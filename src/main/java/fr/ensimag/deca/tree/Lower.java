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

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

    /** ADDED CODE **/

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label label = new Label("lower");
        BranchInstruction branchInstruction = new BLT(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"lower");
    }

}
