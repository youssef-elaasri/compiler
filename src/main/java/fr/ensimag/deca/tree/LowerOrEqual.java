package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class LowerOrEqual extends AbstractOpIneq {

    private static int counter = 0;
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the less-than or equal comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        increaseCounter();
        Label label = new Label("lower_or_equal_"+i);
        BranchInstruction branchInstruction = new BLE(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"lower_or_equal_"+i);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpEq(compiler,false);
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
