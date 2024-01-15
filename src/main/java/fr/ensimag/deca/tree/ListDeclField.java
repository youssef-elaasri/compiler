package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.util.HashMap;
import java.util.Map;

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


    public void verifyListDeclFieldI(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId){
        throw new UnsupportedOperationException("not yet implemented");
    }


}
