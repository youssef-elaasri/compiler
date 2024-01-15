package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam{

    final private AbstractIdentifier type;
    public DeclParam(AbstractIdentifier type){
        Validate.notNull(type);
        this.type = type;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected Type verifyParam(DecacCompiler compiler) throws ContextualError {
        Type typeP = this.type.verifyType(compiler);
        if (typeP.isVoid()){
            throw new ContextualError("Type of param must not be of Void type !", this.getLocation());
        }
        return typeP;
    }

    @Override
    protected ExpDefinition verifyParamName(DecacCompiler compiler) {
        return null;
    }
}
