package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam list_param;
    private boolean isOverride = false;

    private int methodIndex;



    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam list_param) {
        Validate.notNull(type);
        Validate.notNull(list_param);
        Validate.notNull(methodName);
        this.type=type;
        this.methodName = methodName;
        this.list_param=list_param;

    }
    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    public boolean isOverride() {
        return isOverride;
    }

    @Override
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId, ClassDefinition classDef) throws ContextualError {
        ClassDefinition envSup = (ClassDefinition) compiler.environmentType.defOfType(superId.getName());

        Type typeM = type.verifyType(compiler);
        if(envSup == null){
            throw new ContextualError("Super class"+superId.getName()+"is not defined !", superId.getLocation());
        }
        MethodDefinition envExpSuper = (MethodDefinition) envSup.getMembers().getExpDefinitionMap().get(methodName.getName());

        if(envExpSuper != null){
            this.isOverride = true;
           // MethodDefinition methDef = superId.getClassDefinition().getMembers().;
            classDef.incrNbrOfOverrides();
            Signature sig2=envExpSuper.getSignature();
            Type type2=envExpSuper.getType();
            Signature signature = list_param.verifyListDeclParam(compiler);
            if(!sig2.equals(signature)){
                throw new ContextualError(methodName.getName()+" not same signature", getLocation());
            }
            if(! type2.isSubType( compiler.environmentType,typeM)){
                throw new ContextualError(methodName.getName()+" not subtype ", getLocation());
            }
            this.setIndex(envExpSuper.getIndex());
        }

        EnvironmentExp envExp=new EnvironmentExp(null);

        MethodDefinition methDefReturned= new MethodDefinition(typeM, getLocation(), list_param.getSignature(),this.methodIndex);
        methDefReturned.setLabel(new Label(methodName.getName().toString()));
        envExp.declare(methodName.getName(), methDefReturned);
        return envExp;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }

    @Override
    protected AbstractIdentifier getMethodName() {
        return this.methodName;
    }

    @Override
    protected AbstractIdentifier getMethodType() {
        return this.type;
    }


    @Override
    protected void setIndex(int index) {
        this.methodIndex = index;
    }

    @Override
    protected int getIndex() {
        return this.methodIndex;
    }
}