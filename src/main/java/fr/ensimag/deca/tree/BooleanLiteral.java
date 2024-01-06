package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    /** ADDED CODE **/

    /**
     * Generates assembly code to load a constant value onto the stack.
     * The constant value is loaded into the current register,
     * and the register count in the stack is increased.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    @Override
    public void codeGenInst(DecacCompiler compiler) {
        // Load the constant value onto the stack using the specified register
        compiler.addInstruction(new LOAD(
                value,
                Register.getR(compiler.getStack().getCurrentRegister())
        ));
        compiler.getStack().increaseRegister();
    }
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
       if(value)
           compiler.addInstruction(new WSTR("true"));
       else
            compiler.addInstruction(new WSTR("false"));

    }


}
