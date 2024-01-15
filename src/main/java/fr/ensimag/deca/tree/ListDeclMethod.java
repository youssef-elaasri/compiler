package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
       for(AbstractDeclMethod m:getList()){
        m.decompile(s);
        s.println();
       }

    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superId){
//        for(AbstractDeclMethod meth:this.getList()){
//            meth.verifyMethod(compiler, superId);
//        }
        return null;
    }
    public void verifyListDeclMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId){
        throw new UnsupportedOperationException("not yet implemented");
    }

}
