package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{

    final private AbstractIdentifier type;
    final private AbstractIdentifier FieldName;
    final private AbstractInitialization initialization;

    private int offset;

    public DeclField(AbstractIdentifier type, AbstractIdentifier FieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(FieldName);
        Validate.notNull(initialization);
        this.type = type;
        this.FieldName = FieldName;
        this.initialization = initialization;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public AbstractInitialization getInitialization() {
        return initialization;
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
    protected ExpDefinition verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) {
        return null;
    }

    @Override
    protected void verifyFieldInit(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }

    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        //TODO I can't find the field's type
    }
}
