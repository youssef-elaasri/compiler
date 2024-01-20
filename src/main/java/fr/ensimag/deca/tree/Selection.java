package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Selection extends AbstractLValue {

    private final AbstractExpr expression;
    private final AbstractIdentifier fieldIdent;

    public Selection(AbstractExpr expression, AbstractIdentifier fieldIdent) {
        Validate.notNull(expression);
        Validate.notNull(fieldIdent);
        this.expression = expression;
        this.fieldIdent = fieldIdent;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        ClassType classType2 = (ClassType)expression.verifyExpr(compiler,localEnv,currentClass);
        EnvironmentExp envExp2 = ((ClassDefinition) compiler.environmentType.defOfType(classType2.getName())).getMembers();
        if(envExp2 == null){
            throw new ContextualError("Class: "+ classType2.getName() +" is not defined in local environment", this.getLocation());
        }
        Type fieldIdentType = fieldIdent.verifyExpr(compiler, envExp2, currentClass);
        FieldDefinition fieldDefinition = fieldIdent.getFieldDefinition();

        if (fieldDefinition.getVisibility().equals(Visibility.PROTECTED)){
            if (currentClass.getType() == null){
                throw new ContextualError("Cannot get access to field " + fieldIdent.getName() +
                        " from current class", this.getLocation());
            }

            ClassType classType = currentClass.getType();
            if(!classType.isSubType(compiler.environmentType, classType2)){
                throw new ContextualError("Cannot get access to field " + fieldIdent.getName() +
                        " from current class", this.getLocation());
            }
            ClassType classField = fieldDefinition.getContainingClass().getType();
            if(!classField.isSubType(compiler.environmentType, classType)){
                throw new ContextualError("Cannot get access to field " + fieldIdent.getName() +
                        " from current class", this.getLocation());
            }
        }
        this.setType(fieldIdentType);
        return fieldIdentType;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        fieldIdent.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        fieldIdent.iter(f);
    }

    protected void codeGenInstGeneral(DecacCompiler compiler) {
        expression.codeGenInst(compiler);
        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new BEQ(
                compiler.getErrorHandler().addDereferencingNull()
        ));
        compiler.getStack().increaseRegister();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenInstGeneral(compiler);
        compiler.addInstruction(new LOAD(
                new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(),
                        Register.getR(compiler.getStack().getCurrentRegister()-1)),
                Register.getR(compiler.getStack().getCurrentRegister()-1))
        );

    }

    protected DAddr codeGenInstAssign(DecacCompiler compiler) {
        codeGenInstGeneral(compiler);
        return new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(),
                Register.getR(compiler.getStack().getCurrentRegister()-1));
    }
}
