package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;

/**
 * Operator "x >= y"
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    private static int counter = 0;

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the greater-than or equal comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        increaseCounter();
        Label label = new Label("greater_or_equal_"+ i);
        BranchInstruction branchInstruction = new BGE(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"greater_or_equal_"+i);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpEq(compiler,true);
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
