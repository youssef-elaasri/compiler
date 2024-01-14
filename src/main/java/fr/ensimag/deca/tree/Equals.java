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

    /** ADDED CODE **/

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
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new BooleanLiteral(((IntLiteral) rightValue).getValue() == ((IntLiteral) leftValue).getValue());
            }
            if (leftValue instanceof FloatLiteral) {
                return new BooleanLiteral(((IntLiteral) rightValue).getValue() == ((FloatLiteral) leftValue).getValue());
            }
        }
        else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new BooleanLiteral(((FloatLiteral) rightValue).getValue() == ((IntLiteral) leftValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                return new BooleanLiteral(((FloatLiteral) rightValue).getValue() == ((FloatLiteral) leftValue).getValue());
            }
        }
        return null;
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
