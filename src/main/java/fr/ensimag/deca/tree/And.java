package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import org.antlr.v4.runtime.atn.SemanticContext;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label endAnd = new Label("end_and");
        Label isFalse = new Label("is_false");
        Label isTrue = new Label("is_true");


        super.getLeftOperand().codeGenInst(compiler);

        super.compareAndJump(0, compiler.getStack().getCurrentRegister() - 1, endAnd, compiler);

        compiler.getStack().decreaseRegister();

        super.getRightOperand().codeGenInst(compiler);

        super.compareAndJump(0, compiler.getStack().getCurrentRegister() - 1, isFalse, compiler);




        super.boolLabel(1, isTrue, endAnd, compiler);
        super.boolLabel(0, isFalse, endAnd, compiler);

        compiler.addLabel(endAnd);


    }


}
