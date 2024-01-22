package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import org.antlr.v4.runtime.atn.SemanticContext;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    private static int counter = 0;

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    /**
     * Generates code for a logical AND operation.
     * If both the left and right operands are true, the result is true.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        // Create labels for the end of the AND operation, true condition, and false condition
        Label isFalse = new Label("is_false_"+i);
        Label isTrue = new Label("is_true_"+i);
        Label endAnd = new Label("end_and_"+i);

        // Generate code for the left operand
        super.getLeftOperand().codeGenInst(compiler);

        // Compare the result of the left operand with 0 and jump to endAnd if equal (false)
        super.compareAndJump(0, compiler.getStack().getCurrentRegister() - 1, endAnd, compiler);

        // Decrease the register count for the left operand
        compiler.getStack().decreaseRegister();

        // Generate code for the right operand
        super.getRightOperand().codeGenInst(compiler);

        // Compare the result of the right operand with 0 and jump to isFalse if equal (false)
        super.compareAndJump(0, compiler.getStack().getCurrentRegister() - 1, isFalse, compiler);

        // If both operands are true, jump to isTrue
        super.boolLabel(1, isTrue, endAnd, compiler);

        // If either operand is false, jump to isFalse
        super.boolLabel(0, isFalse, endAnd, compiler);

        // Add the label for the end of the AND operation
        compiler.addLabel(endAnd);
    }


}
