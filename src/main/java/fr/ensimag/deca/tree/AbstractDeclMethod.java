package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractDeclMethod extends Tree {
    protected abstract EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId) throws ContextualError,  EnvironmentExp.DoubleDefException;

    protected abstract void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId);

    protected abstract AbstractIdentifier getMethodName();

    protected abstract AbstractIdentifier getMethodType();

    protected abstract Signature getMethodSignature();
}
