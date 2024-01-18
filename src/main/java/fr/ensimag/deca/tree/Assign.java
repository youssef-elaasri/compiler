package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    private static final Logger LOG = Logger.getLogger(Assign.class);


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
            if (compiler.getCompilerOptions().getOPTIM()) {
                LOG.debug("Let's optimize that assign");
                getRightOperand().codeGenInstOP(compiler);
            }
            else
                getRightOperand().codeGenInst(compiler);

            AbstractIdentifier lvalue = (AbstractIdentifier) getLeftOperand();
            compiler.addInstruction(
                    new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1),
                            lvalue.getExpDefinition().getOperand()
                    )
            );
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        LOG.debug("This is codeGen assign");
        if(!compiler.isVariableInDict((AbstractIdentifier) getLeftOperand())){
            codeGenInst(compiler);
            return;
        }

        if(getRightOperand() instanceof AbstractIdentifier && compiler.isVariableInDict((AbstractIdentifier) getRightOperand())){
            compiler.addInstruction(new LOAD(
                    compiler.getRegister((AbstractIdentifier) getRightOperand()),
                    compiler.getRegister((AbstractIdentifier) getLeftOperand())

            ));
        }

        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {

            getRightOperand().codeGenInstOP(compiler);



            compiler.addInstruction(new LOAD(
                    Register.getR(compiler.getStack().getCurrentRegister()-1),
                    compiler.getRegister((AbstractIdentifier) getLeftOperand())
            ));

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
        if (compiler.getIsCritical()) {
            compiler.getIfManager().putIfAbsent((Identifier) getLeftOperand(),
                    ((Identifier) getLeftOperand()).getExpDefinition().getValue()
            );
        };
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue != null) {
            setRightOperand(rightValue);
            ((Identifier) getLeftOperand()).getExpDefinition().setValue(rightValue);
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        ((Identifier) getLeftOperand()).getExpDefinition().setValue(null);
    }


}
