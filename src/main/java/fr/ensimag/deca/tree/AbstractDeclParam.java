package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;

public abstract class AbstractDeclParam extends Tree{
    protected abstract Type verifyParam(DecacCompiler compiler);
    protected abstract ExpDefinition verifyParamName(DecacCompiler compiler);

}
