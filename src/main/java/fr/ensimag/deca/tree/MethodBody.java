package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;


import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

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

    protected void codeGenMethods(DecacCompiler compiler, String className,
                                  String methodName, boolean isVoid) {
        ImmediateInteger integer = new ImmediateInteger(0);
        IMAProgram myProgram = compiler.getProgram();
        IMAProgram copyProgram = new IMAProgram();

        compiler.getStack().resetCurrentRegister();
        compiler.setMethod(className + "." + methodName);
        compiler.getStack().resetTSTO();
        compiler.getStack().resetMaxRegister();

        compiler.addInstruction(new TSTO(integer));
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addStackOverflowError()));
        compiler.setProgram(copyProgram);
        listDeclVar.codeGenMethods(compiler);
        listInst.codeGenListInst(compiler);
        if (!isVoid) {
            compiler.addInstruction(new WSTR("\"Error: exit from method " +
                                            className+ "." + methodName + " without return.\""));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        compiler.addLabel(new Label("end." + className + "." + methodName));
        DeclClass.pushAndPopRegister(compiler, integer, myProgram, copyProgram);
    }
    


    
}
