package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.lang.reflect.Field;

public class DeclField extends AbstractDeclField{
    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    private int offset;



    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    public AbstractIdentifier getFieldName() {
        return fieldName;
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
        // this.visib.decompile...
        s.print(visibility.toString());
        s.print(" ");
        type.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        initialization.decompile(s);
        s.print(";");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }
    @Override
    String prettyPrintNode() {
        return "[visibility = " + this.visibility + "] " + this.getClass().getSimpleName();
    }

    @Override
    protected EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier currentClass) throws ContextualError{
        Type typeF = type.verifyType(compiler);
        if (typeF.isVoid()) {
            throw new ContextualError("Type of field must not be of Void type !", this.getLocation());
        }
        ClassDefinition envSup = superId.getClassDefinition();
        if (envSup == null) {
            throw new ContextualError("Super class " + superId.getName() + " is not defined !", superId.getLocation());
        }
        ExpDefinition expDef = envSup.getMembers().getExpDefinitionMap().get(fieldName.getName());
        if (expDef != null && !(expDef.isField())) {
            throw new ContextualError(fieldName.getName() + " must be of type Field : " + expDef.getType() + " was given !", fieldName.getLocation());
        }
        FieldDefinition fieldDef = new FieldDefinition(typeF, this.getLocation(), visibility, currentClass.getClassDefinition(), currentClass.getClassDefinition().getNumberOfFields());
        EnvironmentExp envExp = new EnvironmentExp(null);
        envExp.declare(fieldName.getName(), fieldDef);
        fieldName.setDefinition(fieldDef);
        return envExp;
    }

    @Override
    protected void verifyFieldInit(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError{
        Type typeF = type.verifyType(compiler);
        initialization.verifyInitialization(compiler, typeF, localEnv, currentClass);
    }

    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        // nothing to do
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = this.fieldName.getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
}
