package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        Type operandType = this.getOperand().getType();
        if (operandType.isInt()){
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError("ConvFloat only possible for INT type and not with type: " +  operandType, this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
