package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;


import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class MethodBody extends Tree{
    final private ListDeclVar listDeclVar;
    final private ListInst listInst;


    public MethodBody(ListDeclVar listDeclVar, ListInst listInst){
        Validate.notNull(listDeclVar);
        Validate.notNull(listInst);
        this.listDeclVar=listDeclVar;
        this.listInst=listInst;
    }

    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,EnvironmentExp enxExpParam, ClassDefinition currentClass,  Type returnType) throws ContextualError {
        EnvironmentExp localEnvVariable = new EnvironmentExp(localEnv);
        localEnvVariable.setExpDefinitionMap(enxExpParam.getExpDefinitionMap());
        listDeclVar.verifyListDeclVariable(compiler, localEnvVariable, currentClass);

        EnvironmentExp localEnvInst = new EnvironmentExp(localEnv);
        localEnvInst.setExpDefinitionMap(localEnvVariable.getExpDefinitionMap());
//        for(Map.Entry<Symbol, ExpDefinition> entry: localEnvVariable.getExpDefinitionMap().entrySet()){
//            if (localEnv.get(entry.getKey()) == null){
//                localEnvInst.declare(entry.getKey(), entry.getValue());
//            }
//        }

        listInst.verifyListInst(compiler, localEnvInst, currentClass, returnType);
        
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        this.listDeclVar.decompile(s);
        this.listInst.decompile(s);
        s.print("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix ,true);
    }




    @Override
    protected void iterChildren(TreeFunction f) {
        listDeclVar.iter(f);
        listInst.iter(f);
    }

    protected void codeGenMethodBody(DecacCompiler compiler) {
    }
    


    
}
