package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
        compiler.addInstruction(new LOAD(Register.getR(1),Register.getR(compiler.getStack().getCurrentRegister())));
        compiler.getStack().increaseRegister();
    }

}
