package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{
    private AbstractIdentifier type;
    private AbstractIdentifier field;
    private AbstractInitialization init;


    @Override
    public void decompile(IndentPrintStream s) {
        // this.visib.decompile...
        s.print(" ");
        type.decompile(s);
        s.print(" ");
        field.decompile(s);
        init.decompile(s);
        s.print(";");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }


    @Override
    protected ExpDefinition verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) {
        return null;
    }

    @Override
    protected void verifyFieldInit(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }
}
