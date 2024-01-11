package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam{
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
    protected Type verifyParam(DecacCompiler compiler) {
        return null;
    }

    @Override
    protected ExpDefinition verifyParamName(DecacCompiler compiler) {
        return null;
    }
}
