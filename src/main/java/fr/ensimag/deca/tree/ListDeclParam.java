package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.ArrayList;
import java.util.List;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    public Signature getSignature() {
        Signature listTypes = new Signature();
        for(AbstractDeclParam param : this.getList()){
            listTypes.add(param.getType());
        }
        return listTypes;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam Param : this.getList()){
            sig.add(Param.verifyParam(compiler));
        }
        return sig;
    }

    public void verifyListParamName(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

}
