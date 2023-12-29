package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        Type type1 = this.getOperand().verifyExpr(compiler, localEnv,currentClass);
        if (type1.isInt()){
            return compiler.environmentType.INT;
        } else if (type1.isFloat()) {
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError(this.getOperatorName() + " unary operation cannot occur with " + type1 + " !", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
