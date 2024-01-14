package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Greater extends AbstractOpIneq {

    private static int counter = 0;

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    /** ADDED CODE **/

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the greater-than comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        increaseCounter();
        Label label = new Label("greater_"+i);
        BranchInstruction branchInstruction = new BGT(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"greater_"+i);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpIn(compiler,true);
    }

    @Override
    public void increaseCounter() {
        counter++;
    }
}
