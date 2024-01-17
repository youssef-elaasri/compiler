package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ListDeclField extends TreeList<AbstractDeclField>{
    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclField f: getList()){
            f.decompile(s);
            s.println();
        }
    }

    public EnvironmentExp verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) throws ContextualError{
//        throw new UnsupportedOperationException("not yet implemented");
        EnvironmentExp envExpr = new EnvironmentExp(null);
        for (AbstractDeclField declF : this.getList()) {
            EnvironmentExp envExp = declF.verifyField(compiler, superId, classId);
            Set<SymbolTable.Symbol> keyS = envExp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keySr = envExpr.getExpDefinitionMap().keySet();
            /*Vérifier si les deux environnements sont disjoints*/
            if (!Collections.disjoint(keyS, keySr)) {
                throw new ContextualError("Vous avez déclaré " + keySr + " plusieurs fois dans la classe !", declF.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());
        }
        return envExpr;
    }


    public void verifyListDeclFieldI(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId){
        throw new UnsupportedOperationException("not yet implemented");
    }


}
