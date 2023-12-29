package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /** ADDED CODE **/

    protected void compareAndJump(int val, int registerNumber, Label label, DecacCompiler compiler) {
        compiler.addInstruction(new CMP(val, Register.getR(registerNumber)));
        compiler.addInstruction(new BEQ(label));
    }

    protected void trueLabel(int val, Label label, Label endlabel, DecacCompiler compiler) {
        compiler.addLabel(label);
        compiler.addInstruction(new LOAD(val,Register.getR(compiler.getStack().getCurrentRegister()-2)));
        compiler.addInstruction(new BRA(endlabel));
        compiler.getStack().decreaseRegister();
    }

}
