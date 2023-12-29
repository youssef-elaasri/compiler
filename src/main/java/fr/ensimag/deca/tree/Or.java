package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    /** ADDED CODE **/

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label trueLabel = new Label("trueLabel");
        Label falseLabel = new Label("falseLabel");
        Label endOr = new Label("endOr");
        getLeftOperand().codeGenInst(compiler);
        compareAndJump(1,compiler.getStack().getCurrentRegister()-1,trueLabel,compiler);
        compiler.getStack().decreaseRegister();
        getRightOperand().codeGenInst(compiler);
        compareAndJump(1,compiler.getStack().getCurrentRegister()-1,trueLabel,compiler);
        boolLabel(0,falseLabel,endOr,compiler);
        boolLabel(1,trueLabel,endOr,compiler);
        compiler.addLabel(endOr);
    }
}
