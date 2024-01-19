package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private final AbstractExpr expression;
    private final AbstractIdentifier methodIdent;
    private final ListExpr listExpression;


    public MethodCall(AbstractExpr expression, AbstractIdentifier methodIdent, ListExpr listExpression) {
        Validate.notNull(expression);
        Validate.notNull(methodIdent);
        Validate.notNull(listExpression);
        this.expression = expression;
        this.methodIdent = methodIdent;
        this.listExpression = listExpression;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        ClassType classType2 = (ClassType) expression.verifyExpr(compiler, localEnv, currentClass);
        EnvironmentExp envExp2 = ((ClassDefinition) compiler.environmentType.defOfType(classType2.getName())).getMembers();
        if(envExp2 == null){
            throw new ContextualError("Class: "+ classType2.getName() +" is not defined in local environment", this.getLocation());
        }
        Type methodIdentType = methodIdent.verifyExpr(compiler, envExp2, currentClass);

        Signature sig = methodIdent.getMethodDefinition().getSignature();
        listExpression.verifyListRValues(compiler, localEnv, currentClass, sig);

        return methodIdentType;
    }



    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        methodIdent.prettyPrint(s, prefix, false);
        listExpression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        methodIdent.iter(f);
        listExpression.iter(f);
    }
}
