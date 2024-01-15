package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.function.Supplier;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        String opName = this.getOperatorName();
        switch (opName) {
            case "%":
                Type typeMod =  this.verifyExpr(compiler, localEnv, currentClass);
                this.setType(typeMod);
                return typeMod;
            default:
                AbstractExpr left = this.getLeftOperand();
                AbstractExpr right = this.getRightOperand();

                Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
                Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
                Type syntType = verifyArithOp(compiler, opName, type1, type2, localEnv, currentClass);
                this.setType(syntType);
                return syntType;
        }
    }

    public Type verifyArithOp(DecacCompiler compiler, String op, Type type1, Type type2, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if ((type1.isInt()) && (type2.isInt())) {
            return compiler.environmentType.INT;
        }
        if ((type1.isInt()) && (type2.isFloat())) {
            AbstractExpr leftOp = new ConvFloat(this.getLeftOperand());
            this.setLeftOperand(leftOp);
            leftOp.verifyExpr(compiler, localEnv, currentClass);
            return compiler.environmentType.FLOAT;
        }
        if ((type1.isFloat()) && (type2.isInt())) {
            AbstractExpr rightOp = new ConvFloat(this.getRightOperand());
            this.setRightOperand(rightOp);
            rightOp.verifyExpr(compiler, localEnv, currentClass);
            return compiler.environmentType.FLOAT;
        }
        if ((type1.isFloat()) && (type2.isFloat())) {
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError(op + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
    }


    /**
     * Generate code for arithmetic operations involving two operands.
     * Generates instructions to perform arithmetic operations based on the types of operands.
     * Handles cases where additional stack manipulation is required due to the limited number of registers.
     *
     * @param compiler                The DecacCompiler instance managing the compilation process.
     * @param binaryInstructionDValToReg The binary instruction for the arithmetic operation.
     * @param isDiv                   A boolean indicating whether the operation is a division.
     * @param isLoad                  A boolean indicating whether to load the result into the current register.
     */
    public void codeGenInstOpArith(DecacCompiler compiler, BinaryInstructionDValToReg binaryInstructionDValToReg,
                                   boolean isDiv, boolean isLoad) {
        if (compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters()) {
            getRightOperand().codeGenInst(compiler);
            if(getDval(getLeftOperand()) instanceof GPRegister)
                compiler.getStack().increaseRegister();
            else{
                getLeftOperand().codeGenInst(compiler);
            }

            compiler.addInstruction(binaryInstructionDValToReg);
            LOG.debug("I'm this and my type is " + this.getType().isFloat());

             if(this.getType().isFloat() && !isDiv) {
                 LOG.debug("I BOV therefore I exist");
                 if (!compiler.getCompilerOptions().getNoCheck())
                    compiler.addInstruction(new BOV(compiler.getErrorHandler().addOverflow()));
             }

            compiler.getStack().decreaseRegister();
            if (isLoad){
                if(getDval(getLeftOperand()) instanceof GPRegister){
                    compiler.addInstruction(new LOAD(
                            Register.getR(compiler.getStack().getCurrentRegister()),
                            (GPRegister) getDval(getLeftOperand())
                    ));
                    return;
                }

                compiler.addInstruction(new LOAD(
                        Register.getR(compiler.getStack().getCurrentRegister()),
                        Register.getR(compiler.getStack().getCurrentRegister() - 1)
                ));
                }

        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInstOpArith(compiler, binaryInstructionDValToReg, isDiv, false);
            compiler.getStack().decreaseRegister();
            compiler.getStack().popRegister(compiler);
            compiler.getStack().increaseRegister();
        }
    }

}