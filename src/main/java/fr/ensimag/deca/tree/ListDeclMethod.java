package fr.ensimag.deca.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        int indexCounter = 0;
        EnvironmentExp envExpr =  new EnvironmentExp(null);
        for(AbstractDeclMethod meth : this.getList()){
            indexCounter++;
            meth.setIndex(indexCounter);
            EnvironmentExp envExp = meth.verifyMethod(compiler, superId);

            //here we decrease the index counter if the class was override
            if( meth.isOverride()) indexCounter--;

            Set<SymbolTable.Symbol> keyS = envExp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keySr = envExpr.getExpDefinitionMap().keySet();
            /*Vérifier si les deux environnements sont disjoints*/
            if (!Collections.disjoint(keyS, keySr)) {
                throw new ContextualError("Vous avez déclaré la méthode " + keySr + " plusieurs fois dans la classe !", meth.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());

        }
        return envExpr;
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError{
       for(AbstractDeclMethod meth: this.getList()){
        meth.verifyMethodBody(compiler, localEnv, currentClass);
       }
    }

}
