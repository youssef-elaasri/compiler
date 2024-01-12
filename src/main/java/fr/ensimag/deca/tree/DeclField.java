package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{
    final private AbstractIdentifier type;
    final private AbstractIdentifier FieldName;
    final private AbstractInitialization initialization;

    public DeclField(AbstractIdentifier type, AbstractIdentifier FieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(FieldName);
        Validate.notNull(initialization);
        this.type = type;
        this.FieldName = FieldName;
        this.initialization = initialization;
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
    protected EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) throws ContextualError {
        Type typeF = type.verifyType(compiler);
        if (typeF.isVoid()) {
            throw new ContextualError("Type of field must not be of Void type !", this.getLocation());
        }
        ClassDefinition envSup = superId.getClassDefinition();
        if (envSup == null) {
            throw new ContextualError("Super class " + superId.getName() + " is not defined !", superId.getLocation());
        }
        ExpDefinition expDef = envSup.getMembers().getExpDefinitionMap().get(FieldName.getName());
        if (expDef == null) {
            throw new ContextualError(FieldName.getName() + " is not defined !", FieldName.getLocation());
        }
        if (!expDef.isField()) {
            throw new ContextualError(FieldName.getName() + " must be of type Field : " + expDef.getType() + " was given !", FieldName.getLocation());
        }
        FieldDefinition fieldDef = new FieldDefinition(typeF, this.getLocation(), classId.getFieldDefinition().getVisibility(), classId.getClassDefinition(), classId.getFieldDefinition().getIndex());
        EnvironmentExp envExp = new EnvironmentExp(null);
        envExp.declare(FieldName.getName(), fieldDef);
        return envExp;
    }

    @Override
    protected void verifyFieldInit(DecacCompiler compiler, ExpDefinition localEnv, ClassDefinition classId) {

    }
}
