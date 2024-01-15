package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    private static int counter = 0;

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


    /**
     * Generates code for a logical OR operation.
     * If the left operand or the right operand is true, the result is true.
     *
     * @param compiler The {@link DecacCompiler}  instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Create labels for true, false, and end of OR operation
        Label trueLabel = new Label("true_Label");
        Label falseLabel = new Label("false_Label");
        Label endOr = new Label("end_Or");

        // Generate code for the left operand
        if (compiler.getCompilerOptions().getOPTIM())
            getLeftOperand().codeGenInstOP(compiler);
        else
            getLeftOperand().codeGenInst(compiler);

        // Compare the result of the left operand with 1 and jump to trueLabel if equal
        compareAndJump(1, compiler.getStack().getCurrentRegister() - 1, trueLabel, compiler);

        // Decrease the register count for the left operand
        compiler.getStack().decreaseRegister();

        // Generate code for the right operand
        if (compiler.getCompilerOptions().getOPTIM())
            getRightOperand().codeGenInstOP(compiler);
        else
            getRightOperand().codeGenInst(compiler);

        // Compare the result of the right operand with 1 and jump to trueLabel if equal
        compareAndJump(1, compiler.getStack().getCurrentRegister() - 1, trueLabel, compiler);

        // If both operands are false, jump to falseLabel
        boolLabel(0, falseLabel, endOr, compiler);

        // If either operand is true, jump to trueLabel
        boolLabel(1, trueLabel, endOr, compiler);

        // Add the label for the end of the OR operation
        compiler.addLabel(endOr);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationBool(compiler,false);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    public void increaseCounter() {
        counter++;
    }
}
