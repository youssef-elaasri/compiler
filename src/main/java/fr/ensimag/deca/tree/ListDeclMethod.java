package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");

    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superId){
        throw new UnsupportedOperationException("not yet implemented");
    }
    public void verifyListDeclMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId){
        throw new UnsupportedOperationException("not yet implemented");
    }

}
