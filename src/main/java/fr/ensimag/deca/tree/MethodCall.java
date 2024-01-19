package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private final AbstractExpr expression;
    private final AbstractIdentifier methodIdent;
    private final ListExpr listExpression;

    public MethodCall(AbstractExpr expression, AbstractIdentifier methodIdent, ListExpr listExpression) {
        Validate.notNull(expression);
        Validate.notNull(methodIdent);
        Validate.notNull(listExpression);
        this.expression = expression;
        this.methodIdent = methodIdent;
        this.listExpression = listExpression;
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
        methodIdent.prettyPrint(s, prefix, false);
        listExpression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        methodIdent.iter(f);
        listExpression.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int size = listExpression.size();

        compiler.addComment("Call Method " + methodIdent.getName());
        compiler.addInstruction(new ADDSP(size + 1));
        //TODO
        compiler.addInstruction(new LOAD(
                expression.
        ));

        compiler.addInstruction(new STORE(
                Register.getR(compiler.getStack().getCurrentRegister()),
                new RegisterOffset(0, Register.SP)
        ));

        AbstractExpr abstractExpr;
        for(int i = size - 1; i >= 0; --i){
            abstractExpr = listExpression.getList().get(i);

            abstractExpr.codeGenInst(compiler);
            compiler.getStack().decreaseRegister();

            compiler.addInstruction(new STORE(
                    Register.getR(compiler.getStack().getCurrentRegister()),
                    new RegisterOffset(i - size, Register.SP)
            ));
        }

        compiler.addInstruction(new LOAD(
                new RegisterOffset(0, Register.SP),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));

        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));

        compiler.addInstruction();


    }
}
