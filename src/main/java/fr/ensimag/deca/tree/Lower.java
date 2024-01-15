package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Lower extends AbstractOpIneq {

    private static int counter = 0;

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the less-than comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        Label label = new Label("lower_"+i);
        increaseCounter();
        BranchInstruction branchInstruction = new BLT(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"lower_" + i);
    }

    @Override
    public void increaseCounter() {
        counter++;
    }

}
