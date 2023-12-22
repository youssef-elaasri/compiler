package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.Tree;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException, IOException {
        // A FAIRE : parcourir args pour positionner les options correctement.

        switch (args[0]){
            case "-p":
                String[] file_name = new String[1];
                file_name[0] = args[1];
                DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(file_name);
                CommonTokenStream tokens = new CommonTokenStream(lex);
                DecaParser parser = new DecaParser(tokens);
                File file = null;
                if (lex.getSourceName() != null) {
                    file = new File(lex.getSourceName());
                }
                //sourceFiles.add(file);
                final DecacCompiler decacCompiler = new DecacCompiler(new CompilerOptions(), file);
                parser.setDecacCompiler(decacCompiler);
                Program prog = (Program)parser.parseProgramAndManageErrors(System.err);
                if (prog == null) {
                    System.exit(1);
                } else {
                    prog.decompile(System.out);
                }
            default: break;
        }



        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");

    }

    protected void displayUsage() {
        //throw new UnsupportedOperationException("not yet implemented");
    }
}
