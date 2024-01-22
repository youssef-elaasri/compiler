package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
        //throw new UnsupportedOperationException("not yet implemented");
        Type type1 = this.getOperand().verifyExpr(compiler, localEnv,currentClass);
        if (type1.isBoolean()){
            this.setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(this.getOperatorName() + " unary operation cannot occur with " + type1 + " !", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }



    /**
     * Generates code for a logical NOT operation.
     * If the operand is false, the result is true; otherwise, the result is false.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Generate code for the operand
        super.getOperand().codeGenInst(compiler);


        // Compare the result of the operand with 0 and jump to falseNot if equal (operand is false)
        compiler.addInstruction(new CMP(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addInstruction(new SEQ(Register.getR(compiler.getStack().getCurrentRegister() - 1)));


    }

}
