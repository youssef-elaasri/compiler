package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree{

    protected abstract EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier currentClass) throws ContextualError;

    protected abstract void verifyFieldInit(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError ;

    public abstract void codeGenInitListDeclClass(DecacCompiler compiler);
}
