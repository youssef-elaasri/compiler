package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * read...() statement.
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    protected void moveToRegister (DecacCompiler compiler) {
        compiler.addInstruction(new PUSH(Register.getR(1)));
        Stack.increaseCounterTSTO();
        compiler.addInstruction(new POP(Register.getR(compiler.getStack().getCurrentRegister())));
        Stack.decreaseCounterTSTO();
        compiler.getStack().increaseRegister();
    }

}
