package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.lang.reflect.Field;

public class DeclField extends AbstractDeclField{
    final private AbstractIdentifier visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    private int offset;
    public DeclField(AbstractIdentifier visibility, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
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
        // this.visib.decompile...
        visibility.decompile(s);
        s.print(" ");
        type.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        initialization.decompile(s);
        s.print(";");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        visibility.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        visibility.iter(f);
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }


    @Override
    protected EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) throws ContextualError{
        Type typeF = type.verifyType(compiler);
        if (typeF.isVoid()) {
            throw new ContextualError("Type of field must not be of Void type !", this.getLocation());
        }
        ClassDefinition envSup = superId.getClassDefinition();
        if (envSup == null) {
            throw new ContextualError("Super class " + superId.getName() + " is not defined !", superId.getLocation());
        }
        ExpDefinition expDef = envSup.getMembers().getExpDefinitionMap().get(fieldName.getName());
        FieldDefinition fieldDef;
        if (expDef == null) {
            fieldDef = new FieldDefinition(typeF, this.getLocation(), visibility.getVisibility(compiler), classId.getClassDefinition(), 0);
        }
        else {
            if (!expDef.isField()) {
                throw new ContextualError(fieldName.getName() + " must be of type Field : " + expDef.getType() + " was given !", fieldName.getLocation());
            }
            else {
                fieldDef = (FieldDefinition) expDef;
            }
        }
        EnvironmentExp envExp = new EnvironmentExp(null);
        envExp.declare(fieldName.getName(), fieldDef);
        fieldName.setDefinition(fieldDef);
        return envExp;
    }

    @Override
    protected void verifyFieldInit(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }

    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        //TODO I can't find the field's type
    }
}
