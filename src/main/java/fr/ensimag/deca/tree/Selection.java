package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
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
        return null;
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
