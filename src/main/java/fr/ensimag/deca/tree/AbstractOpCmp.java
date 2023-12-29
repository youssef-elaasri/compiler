package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {



    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
      //  throw new UnsupportedOperationException("not yet implemented");
        String opName = this.getOperatorName();

        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
        Type syntType = verifyCmpOp(compiler, opName, type1, type2);
        return syntType;
    }

    public Type verifyCmpOp(DecacCompiler compiler, String op, Type type1, Type type2) throws ContextualError {
        List<String> opList = new ArrayList<String>();
        opList.add("==");
        opList.add("!=");
        opList.add("<");
        opList.add("<=");
        opList.add(">");
        opList.add(">=");


        //here we verify if type1 and type2 are in dom(type_arith_op) and if op is in the opList
        if( (opList.contains(op) && verifyDomTypeArithOp(type1) && verifyDomTypeArithOp(type2))
        //here we verify if type1 and type2 are both boolean and if the op is eq or neq
        || ( (type1.isBoolean() && type2.isBoolean()) && (op.equals("==") || op.equals("!=")) ) ) {
            return compiler.environmentType.BOOLEAN;
        }
        //TO DO Add the case where op ∈ {eq, neq} and T1 = type_class(A)
        throw new ContextualError(op + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());



    }

    public boolean verifyDomTypeArithOp(Type type){
        return (type.isInt() || type.isFloat());
    }
}
