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
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId) throws ContextualError, EnvironmentExp.DoubleDefException {

        return null;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }

    @Override
    protected AbstractIdentifier getMethodName() {
        return this.name;
    }

    @Override
    protected AbstractIdentifier getMethodType() {
        return this.type;
    }

    @Override
    protected Signature getMethodSignature() {
        return this.sig;
    }
}