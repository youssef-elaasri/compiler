package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import org.apache.log4j.Logger;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {

    private static final Logger LOG = Logger.getLogger(Plus.class);

    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions to perform addition operation based on the types of operands.
     * If the right operand is a constant or variable, it directly adds it to the left operand.
     * If the right operand is not a constant or variable, it generates instructions to perform
     * the addition operation and updates the compiler's stack accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal dVal = getDval(getRightOperand());
        if (dVal != null) {
            if (compiler.getCompilerOptions().getOPTIM())
                getLeftOperand().codeGenInstOP(compiler);
            else
                getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(new ADD(dVal,
                    Register.getR(compiler.getStack().getCurrentRegister()-1)));
            if(this.getType().isFloat())
                if (!compiler.getCompilerOptions().getNoCheck())
                    compiler.addInstruction(new BOV(compiler.getErrorHandler().addOverflow()));

        }
        else {
            int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                    1 : 0;
            BinaryInstructionDValToReg binaryInstructionDValToReg = new ADD(Register.getR(compiler.getStack().getCurrentRegister() + registerDec -1),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec));
            codeGenInstOpArith(compiler,binaryInstructionDValToReg,false, true);
        }
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        DVal rightDVal = getDval(getRightOperand());
        DVal lefttDVal = getDval(getLeftOperand());

        LOG.debug("Hey! did you know that rightDVal is" + rightDVal);
        if (rightDVal != null) {
            // Call register to optimize variable usage
            LOG.debug("Hey! did you know that lefttDVal instanceof GPRegister is " + (lefttDVal instanceof GPRegister));
            if(lefttDVal instanceof GPRegister){
                LOG.debug("Hey! leftDval is a register" + lefttDVal);
                compiler.getStack().increaseRegister();
                compiler.addInstruction(new ADD(
                        rightDVal,
                        (GPRegister) lefttDVal
                ));
            }else {
                // no optimization is done here
                getLeftOperand().codeGenInst(compiler);
                compiler.addInstruction(new ADD(
                        rightDVal,
                        Register.getR(compiler.getStack().getCurrentRegister() - 1)
                ));
            }

            if(this.getType().isFloat())
                if (!compiler.getCompilerOptions().getNoCheck())
                    compiler.addInstruction(new BOV(compiler.getErrorHandler().addOverflow()));

        }
        else {
            int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                    1 : 0;

            BinaryInstructionDValToReg binaryInstructionDValToReg;

            if(lefttDVal instanceof GPRegister) {
                 binaryInstructionDValToReg = new ADD(
                        Register.getR(compiler.getStack().getCurrentRegister() + registerDec - 1),
                        (GPRegister) lefttDVal
                );
            }
            else {
                 binaryInstructionDValToReg = new ADD(
                        Register.getR(compiler.getStack().getCurrentRegister() + registerDec - 1),
                        Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
                );
            }

            codeGenInstOpArith(compiler,binaryInstructionDValToReg,false, true);
        }
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new IntLiteral(((IntLiteral) leftValue).getValue()+((IntLiteral) rightValue).getValue());
            } else if (leftValue instanceof FloatLiteral) {
                return new FloatLiteral(((FloatLiteral) leftValue).getValue() + ((IntLiteral) rightValue).getValue());
            }
        } else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new FloatLiteral(((IntLiteral) leftValue).getValue() + ((FloatLiteral) rightValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                return new FloatLiteral(((FloatLiteral) leftValue).getValue()+((FloatLiteral) rightValue).getValue());
            }
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }
}
