package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    /** ADDED CODE **/

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal dVal = getDval(getOperand());
        if ( dVal != null) {
            compiler.addInstruction(new OPP(dVal,
                    Register.getR(compiler.getStack().getCurrentRegister())));
            compiler.getStack().increaseRegister();
        }
        else {
            super.getOperand().codeGenInst(compiler);
            compiler.addInstruction(new OPP(Register.getR(compiler.getStack().getCurrentRegister()-1),
                    Register.getR(compiler.getStack().getCurrentRegister()-1)));
        }
    }
}
