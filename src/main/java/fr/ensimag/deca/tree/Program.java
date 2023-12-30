package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorHandler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
//        throw new UnsupportedOperationException("not yet implemented");
        // LOG.debug("verify program: end");
    }

    /**
     * Generates code for the entire program.
     * This method is called during the code generation phase for the main program.
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // Comment: Start main program
        compiler.addComment("start main program");

        // Initialize the stack with a total stack object (TSTO) value of 1
        // The value is going to be modified at the end to match the actual used stack size
        ImmediateInteger TSTOimmediateInteger = new ImmediateInteger(1);
        compiler.addInstruction(new TSTO(TSTOimmediateInteger));


        // Check for stack overflow and branch to the specified label if overflow occurs
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addStackOverflowError()));

        // Set the stack pointer (SP) to 0
        ImmediateInteger SPimmediateInteger = new ImmediateInteger(0);
        compiler.addInstruction(new ADDSP(SPimmediateInteger));

        compiler.addComment("Main program");
        main.codeGenMain(compiler);

        // Halt the program execution
        compiler.addInstruction(new HALT());
        compiler.addComment("end main program");

        // Add error labels and associate them with their corresponding error messages
        compiler.getErrorHandler().putErrors(compiler);

        // Update TSTO and SP values based on stack information
        TSTOimmediateInteger.setValue(Math.max(compiler.getStack().getMaxTSTO(), compiler.getStack().getCounterTSTO()));
        SPimmediateInteger.setValue(compiler.getStack().getAddrCounter()-1);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
