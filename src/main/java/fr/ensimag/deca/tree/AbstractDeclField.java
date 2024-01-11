package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree{

    protected abstract ExpDefinition verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId);

    protected abstract void verifyFieldInit(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId);

}
