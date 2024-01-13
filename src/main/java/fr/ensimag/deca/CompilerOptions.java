package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.*;
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

    /**
     * verifie if we should decompile the file or not (option -p)
     * @return
     */
    public boolean getParse() { return parse; }

    /**
     * verifie if we should stop decac after the verification phase or not (option -v)
     * @return
     */
    public boolean getVerification() { return verification; }

    public boolean getNoCheck(){
        return noCheck;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;

    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException, IOException {
        // A FAIRE : parcourir args pour positionner les options correctement.

        for (String arg : args){
            if (arg.startsWith("-")){
                switch (arg){
                    case "-p":
                        if (this.verification){
                            throw new CLIException("option -v and -p cannot be taken together");
                        }
                        else{
                            this.parse = true;
                        }
                        break;
                    case "-v":
                        if (this.parse){
                            throw new CLIException("option -p and -v cannot be taken together");
                        }
                        else{
                            this.verification = true;
                        }
                        break;
                    case "-P":
                        this.parallel = true;
                        break;
                    case "-b":
                        this.printBanner = true;
                        break;
                    case "-d":
                        this.debug++;
                        break;
                    case "-n":
                        noCheck=true;
                        break;
                    default:
                        throw new CLIException("the option: " + arg + " does not exist");
                }
            } else{
                this.sourceFiles.add(new File(arg));
            }
        }

//        String[] file_name = new String[1];
//        int argsLength  = args.length;
//
//        if (argsLength > 0) {
//            file_name[0] = args[argsLength-1];
//        }
//        else {
//            throw new CLIException("No options or file name are given.");
//        }
//
//        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(file_name);
//        CommonTokenStream tokens = new CommonTokenStream(lex);
//        DecaParser parser = new DecaParser(tokens);
//        File file = null;
//
//        if (lex.getSourceName() != null) {
//            file = new File(lex.getSourceName());
//        }
//        else{
//            throw new CLIException("Cannot find file: " + file_name[0]);
//        }
//        sourceFiles.add(file);
//        final DecacCompiler decacCompiler = new DecacCompiler(new CompilerOptions(), file);
//        parser.setDecacCompiler(decacCompiler);
//        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
//
//        switch (args[0]) {
//            case "-p":
//                if (prog == null) {
//                    System.exit(1);
//                } else {
//                    prog.decompile(System.out);
//                    System.exit(1);
//                }
//                break;
//            case "-v":
//                if (prog == null) {
//                    System.exit(1);
//                } else {
//                    try {
//                        prog.verifyProgram(decacCompiler);
//                        System.exit(1);
//                    } catch (LocationException e) {
//                        e.display(System.err);
//                        System.exit(1);
//                    }
//                }
//                break;
//            default:
//                File srcFile = new File(args[0]);
//        }

        Logger logger = Logger.getRootLogger();
        //map command-line debug option to log4j's level.
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
        //TODO
        //throw new UnsupportedOperationException("not yet implemented");
    }
}
