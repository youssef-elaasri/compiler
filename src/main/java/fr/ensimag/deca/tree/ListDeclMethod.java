package fr.ensimag.deca.tree;

import java.util.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    private final int NbrOfAllMethods = this.getList().size();
    private Map<Integer, AbstractDeclMethod> indexMethodMap = new HashMap<Integer, AbstractDeclMethod>();

    @Override
    public void decompile(IndentPrintStream s) {
       for(AbstractDeclMethod m:getList()){
        m.decompile(s);
        s.println();
       }

    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superId, ClassDefinition classDef) throws ContextualError{
        ClassDefinition supClass = (ClassDefinition) compiler.environmentType.defOfType(superId.getName());
        int iterationCounter = 0;
        int indexCounter = supClass.getNumberOfMethods() + supClass.getNbrOfOverrides();
        EnvironmentExp envExpr =  new EnvironmentExp(null);
        int initialIndex = supClass.getNumberOfMethods() + supClass.getNbrOfOverrides();
        for(AbstractDeclMethod meth : this.getList()){
            indexCounter++;
            meth.setIndex(indexCounter);
            EnvironmentExp envExp = meth.verifyMethod(compiler, superId, classDef);

            if( meth.isOverride()){
                //case one if the meth override is first in methods we restart the indexCounter
                if (meth.getIndex() == indexCounter - 1){
                    indexCounter--;
                }
                //case two if the meth override is in the middle of methods
                else if (meth.getIndex() >= initialIndex) {
                    this.getList().get(meth.getIndex() - initialIndex).setIndex(indexCounter);
                }
            }

            Set<SymbolTable.Symbol> keyS = envExp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keySr = envExpr.getExpDefinitionMap().keySet();
            /*Vérifier si les deux environnements sont disjoints*/
            if (!Collections.disjoint(keyS, keySr)) {
                throw new ContextualError("Vous avez déclaré la méthode " + keySr + " plusieurs fois dans la classe !", meth.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());
            indexMethodMap.put(meth.getIndex(), meth);
            iterationCounter++;
        }
        return envExpr;
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass){
        throw new UnsupportedOperationException("not yet implemented");
    }

}
