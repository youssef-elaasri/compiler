package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Instanceof extends AbstractExpr{
    final private AbstractExpr expression;
    final private AbstractIdentifier typeIdent;

    private static int counter = 0;

    public Instanceof(AbstractExpr expression, AbstractIdentifier typeIdent){
        Validate.notNull(expression);
        Validate.notNull(typeIdent);
        this.expression = expression;
        this.typeIdent = typeIdent;

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type expType = expression.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = typeIdent.verifyType(compiler);
        if (!((expType.isNull() || expType.isClass()) && type2.isClass())){
            throw new ContextualError("Using Inctanceof is not valid", this.getLocation());
        }
        return compiler.environmentType.BOOLEAN;
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
    public void codeGenInst(DecacCompiler compiler) {
        int index = counter;
        counter++;
        Label initLabel = new Label("init_instanceof_" + index);
        Label trueInstanceof = new Label("true_instanceof_" + index);
        Label falseInstanceof = new Label("false_instanceof_" + index);
        Label endInstanceof = new Label("end_Instanceof_" + index);
        expression.codeGenInst(compiler);
        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new BEQ(compiler.getErrorHandler().addDereferencingNull()));
        compiler.addLabel(initLabel);
        compiler.addInstruction(new LOAD(
                new RegisterOffset(0, Register.getR(compiler.getStack().getCurrentRegister()-1)),
                Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new CMP(
                new NullOperand(),Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new BEQ(falseInstanceof));
        compiler.addInstruction(new CMP(
                typeIdent.getExpDefinition().getOperand(),
                Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new BEQ(trueInstanceof));
        compiler.addInstruction(new BRA(initLabel));
        compiler.addLabel(trueInstanceof);
        compiler.addInstruction(new LOAD(
                1, Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addInstruction(new BRA(endInstanceof));
        compiler.addLabel(falseInstanceof);
        compiler.addInstruction(new LOAD(
                0, Register.getR(compiler.getStack().getCurrentRegister()-1)
        ));
        compiler.addLabel(endInstanceof);

    }
}
