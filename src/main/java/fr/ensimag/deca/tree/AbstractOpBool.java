package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        List<String> opList = new ArrayList<String>();
        opList.add("&&");
        opList.add("||");

        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);

        if( (type1.isBoolean() && type2.isBoolean()) && opList.contains(this.getOperatorName()) ){
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(this.getOperatorName() + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
    }
}
