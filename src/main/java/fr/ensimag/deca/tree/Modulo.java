package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
        if ((type1.isInt()) && (type2.isInt())) {
            return compiler.environmentType.INT;
        }
        else {
            throw new ContextualError( "Modulo operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
        }
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
