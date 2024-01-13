package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        AbstractLValue left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type inheritedType = left.verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr convRight = right.verifyRValue(compiler, localEnv, currentClass, inheritedType);
        this.setRightOperand(convRight);
        this.setType(inheritedType);
        return inheritedType;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for an assignment operation, storing the value of the right operand
     * into the memory location specified by the left operand.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            getRightOperand().codeGenInst(compiler);
            AbstractIdentifier lvalue = (AbstractIdentifier) getLeftOperand();
            compiler.addInstruction(
                    new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1),
                            lvalue.getExpDefinition().getOperand()
                    )
            );
            compiler.getStack().decreaseRegister();
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

}
