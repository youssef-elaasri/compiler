package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    /** ADDED CODE**/
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal dVal = getDval(getRightOperand());
        if (dVal != null) {
            getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(new ADD(dVal,
                    Register.getR(compiler.getStack().getCurrentRegister()-1)));
        }
        else {
            BinaryInstructionDValToReg binaryInstructionDValToReg = new ADD(Register.getR(compiler.getStack().getCurrentRegister() - 2),
                    Register.getR(compiler.getStack().getCurrentRegister() - 1));
            codeGenInstOpArith(compiler,binaryInstructionDValToReg);
        }
    }
}
