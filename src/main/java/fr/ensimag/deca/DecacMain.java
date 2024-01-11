package fr.ensimag.deca;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: File not found");
        }
        if (options.getPrintBanner()) {
            System.out.println("GL g22");
            System.exit(1);
        }
        if (options.getSourceFiles().isEmpty()) {
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
            throw new UnsupportedOperationException("Source file must be specified with the given options");
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            //throw new UnsupportedOperationException("Parallel build not yet implemented");

            //creating worker threads
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (File source : options.getSourceFiles()) {
                Callable<Boolean> task = () -> {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    return compiler.compile();
                };
                Future<Boolean> future = executorService.submit(task);
                if (future.get()){
                    error = true;
                }
            }
            executorService.shutdown();
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
