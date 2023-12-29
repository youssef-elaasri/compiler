package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BranchInstruction;
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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /** ADDED CODE **/

    protected void codeGenInstGeneral(DecacCompiler compiler,
                                      BranchInstruction branchInstruction, Label trueLabel, String opCmp) {
        if(compiler.getStack().getCurrentRegister()+1 < compiler.getStack().getNumberOfRegisters()) {
            codeGenInstOpCmp(compiler,2,branchInstruction, trueLabel, opCmp);
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInstOpCmp(compiler,1,branchInstruction, trueLabel, opCmp);
            compiler.getStack().decreaseRegister();
            compiler.getStack().popRegister(compiler);
            compiler.getStack().increaseRegister();
        }
    }

    protected void codeGenInstOpCmp(DecacCompiler compiler, int val,
                                    BranchInstruction branchInstruction, Label trueLabel, String opCmp) {
        Label endEqualLabel = new Label("end_" + opCmp);
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new CMP(Register.getR(compiler.getStack().getCurrentRegister()-2),
                Register.getR(compiler.getStack().getCurrentRegister()-1)));
        compiler.addInstruction(branchInstruction);
        compiler.addInstruction(new LOAD(0,Register.getR(compiler.getStack().getCurrentRegister()-val)));
        compiler.addInstruction(new BRA(endEqualLabel));
        compiler.addLabel(trueLabel);
        compiler.addInstruction(new LOAD(1,Register.getR(compiler.getStack().getCurrentRegister()-val)));
        compiler.addLabel(endEqualLabel);
        compiler.getStack().decreaseRegister();
    }

}
