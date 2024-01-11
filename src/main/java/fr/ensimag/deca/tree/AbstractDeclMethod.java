package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;

public abstract class AbstractDeclMethod extends Tree {
    protected abstract ExpDefinition verifyMethod(DecacCompiler compiler, AbstractIdentifier superId);

    protected abstract void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId);


}
