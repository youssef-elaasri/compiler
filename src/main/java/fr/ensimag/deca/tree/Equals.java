package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.util.Random;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {

    private static int counter = 0;

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the equality comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        increaseCounter();
        Label label = new Label("equal_"+ i);
        BranchInstruction branchInstruction = new BEQ(label);
        codeGenInstGeneral(compiler,branchInstruction,label,"equal_" + i);
    }

    @Override
    public void increaseCounter() {
        counter++;
    }

    
}
