package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{
    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private Signature sig;

    public DeclMethod(AbstractIdentifier type,AbstractIdentifier name, Signature sig){
        Validate.notNull(type);
        Validate.notNull(sig);
        Validate.notNull(name);
        this.type=type;
        this.name=name;
        this.sig=sig;

        
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

    @Override
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId) throws ContextualError {
        ClassDefinition envSup = superId.getClassDefinition();
        Type typeM=type.verifyType(compiler);
        if(envSup==null){
            throw new ContextualError("Super class"+superId.getName()+"is not defined !", superId.getLocation());
        }
        ExpDefinition envExpSuper= envSup.getMembers().getExpDefinitionMap().get(name.getName());
        if(envExpSuper == null){
            throw new ContextualError(name.getName() + " is not defined !", name.getLocation());
        }

        MethodDefinition methDef =superId.getMethodDefinition();
        Signature sig2=methDef.getSignature();
        Type type2=methDef.getType();
        if(!sig2.equals(sig)){
            throw new ContextualError(name.getName()+" not same signature", getLocation());
        }
        
        if(! type2.isSubType( compiler.environmentType,typeM)){
            throw new ContextualError(name.getName()+" not subtype ", getLocation());
        }
        EnvironmentExp envExp=new EnvironmentExp(null);
        envSup.incNumberOfMethods();
        MethodDefinition methDefReturned= new MethodDefinition(typeM, getLocation(), sig,envSup.getNumberOfMethods());
        envExp.declare(name.getName(), methDefReturned);
        return envExp;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }
}
