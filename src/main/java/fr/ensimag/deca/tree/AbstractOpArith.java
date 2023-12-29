package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.SUB;

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

    public void codeGenInstOpArith(DecacCompiler compiler, BinaryInstructionDValToReg binaryInstructionDValToReg) {
        if (compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters()) {
            getRightOperand().codeGenInst(compiler);
            getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(binaryInstructionDValToReg);
            compiler.getStack().decreaseRegister();
        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().decreaseRegister();
            compiler.getStack().popRegister(compiler);
            compiler.getStack().increaseRegister();
        }
    }
}
