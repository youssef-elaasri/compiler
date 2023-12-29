package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    /** ADDED CODE **/
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label label = new Label("not_equal");
        BranchInstruction branchInstruction = new BNE(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"not_equal");
    }

}
