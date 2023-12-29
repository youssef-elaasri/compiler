package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }


    /** ADDED CODE **/

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.getOperand().codeGenInst(compiler);
        Label endNot = new Label("end_not");
        Label falseNot = new Label("false_not");

        compiler.addInstruction(new CMP(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addInstruction(new BEQ(falseNot));

        compiler.addInstruction(new LOAD(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addInstruction(new BRA(endNot));

        compiler.addLabel(falseNot);
        compiler.addInstruction(new LOAD(1, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addLabel(endNot);

    }
}
