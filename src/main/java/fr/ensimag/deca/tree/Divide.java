package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    /** ADDED CODE**/

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions to perform division operation based on the types of operands.
     * Generates instructions to perform the division operation and updates the
     * compiler's stack accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                1 : 0;
        BinaryInstructionDValToReg binaryInstructionDValToReg;

        if(this.getType().isFloat()) {
            binaryInstructionDValToReg = new DIV(Register.getR(
                    compiler.getStack().getCurrentRegister() + registerDec - 1),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
            );
        }
        else {
            binaryInstructionDValToReg = new QUO(Register.getR(
                    compiler.getStack().getCurrentRegister() + registerDec - 1),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
            );
        }

        codeGenInstOpArith(compiler, binaryInstructionDValToReg, true, true);

    }

}
