package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
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

    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,  Type returnType) throws ContextualError {
        listDeclVar.verifyListDeclVariable(compiler, localEnv, currentClass);
        listInst.verifyListInst(compiler, localEnv, currentClass, returnType);
        
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
