package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr{
    AbstractIdentifier classNanme;

    public New(AbstractIdentifier abstractIdentifier){
        super();
        classNanme = abstractIdentifier;
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

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new NEW(
                compiler.getClassManager().get(classNanme).getListField().getList().size(),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addFullStack()));
        compiler.addInstruction(
                new LEA(compiler.getClassManager().get(classNanme).getClassName().getExpDefinition().getOperand(),
                Register.R0
                ));
        compiler.addInstruction(
                new STORE(Register.R0,new RegisterOffset(0,
                        Register.getR(compiler.getStack().getCurrentRegister()))
                ));
        compiler.addInstruction(
                new BSR(new Label("init."
                        + compiler.getClassManager().get(classNanme).getClassName().getName())
                ));

        compiler.getStack().increaseRegister();
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }


}
