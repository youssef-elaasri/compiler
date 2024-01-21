package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Instanceof extends AbstractExpr{
    final private AbstractExpr expression;
    final private AbstractIdentifier typeIdent;

    public Instanceof(AbstractExpr expression, AbstractIdentifier typeIdent){
        Validate.notNull(expression);
        Validate.notNull(typeIdent);
        this.expression = expression;
        this.typeIdent = typeIdent;

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type expType = expression.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = typeIdent.verifyType(compiler);
        if (!((expType.isNull() || expType.isClass()) && type2.isClass())){
            throw new ContextualError("Using Instanceof is not valid", this.getLocation());
        }
        this.setType(type2);
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeIdent.prettyPrint(s, prefix, false);
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        typeIdent.iter(f);
        expression.iter(f);
    }
}
