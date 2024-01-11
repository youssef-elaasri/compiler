package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod{
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
    protected ExpDefinition verifyMethod(DecacCompiler compiler, AbstractIdentifier superId) {
        return null;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }
}
