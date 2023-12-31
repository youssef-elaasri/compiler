package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

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
        right.verifyRValue(compiler, localEnv, currentClass, inheritedType);
        this.setType(inheritedType);
        return inheritedType;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
