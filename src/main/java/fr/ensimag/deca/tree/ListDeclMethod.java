package fr.ensimag.deca.tree;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
       for(AbstractDeclMethod m:getList()){
        m.decompile(s);
        s.println();
       }

    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superId) throws ContextualError{
        EnvironmentExp envExpr =  new EnvironmentExp(null);
        for(AbstractDeclMethod meth:this.getList()){
            EnvironmentExp envExp = meth.verifyMethod(compiler, superId);
            Map<SymbolTable.Symbol, ExpDefinition> interMap = new HashMap<>(envExp.getExpDefinitionMap());
            /*Vérifier si les deux environnements sont disjoints*/
            interMap.keySet().retainAll(envExpr.getExpDefinitionMap().keySet());
            if (!interMap.isEmpty()) {
                throw new ContextualError("Vous avez déclaré un champ plusieurs fois dans la classe !", this.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());
        }
        return envExpr;
    }
    public void verifyListDeclMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId){
        throw new UnsupportedOperationException("not yet implemented");
    }

}
