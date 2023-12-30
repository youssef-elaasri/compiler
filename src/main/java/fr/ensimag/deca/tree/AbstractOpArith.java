package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

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

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /** ADDED CODE**/

    public void codeGenInstOpArith(DecacCompiler compiler, BinaryInstructionDValToReg binaryInstructionDValToReg,
                                   boolean isDiv, boolean isLoad) {
        if (compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters()) {
            getRightOperand().codeGenInst(compiler);
            if (isDiv) {
                compiler.addInstruction(new CMP(0,Register.getR(compiler.getStack().getCurrentRegister()-1)));
                compiler.addInstruction(new BEQ(compiler.getErrorHandler().addDivisionByZero()));
            }
            getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(binaryInstructionDValToReg);
            compiler.getStack().decreaseRegister();
            if (isLoad)
                compiler.addInstruction(new LOAD(Register.getR(compiler.getStack().getCurrentRegister()),
                    Register.getR(compiler.getStack().getCurrentRegister()-1)));
        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInstOpArith(compiler, binaryInstructionDValToReg,isDiv,false);
            compiler.getStack().decreaseRegister();
            compiler.getStack().popRegister(compiler);
            compiler.getStack().increaseRegister();
        }
    }
}
