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
        //throw new UnsupportedOperationException("not yet implemented");
        Type type1 = this.getOperand().verifyExpr(compiler, localEnv,currentClass);
        if (type1.isBoolean()){
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(this.getOperatorName() + " unary operation cannot occur with " + type1 + " !", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }


    /** ADDED CODE **/

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

        // Create labels for the end of the NOT operation and the false condition
        Label endNot = new Label("end_not");
        Label falseNot = new Label("false_not");

        // Compare the result of the operand with 0 and jump to falseNot if equal (operand is false)
        compiler.addInstruction(new CMP(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addInstruction(new BEQ(falseNot));

        // If the operand is true, load 0 into the register (result is false) and jump to endNot
        compiler.addInstruction(new LOAD(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
        compiler.addInstruction(new BRA(endNot));

        // Add label for the false condition
        compiler.addLabel(falseNot);

        // If the operand is false, load 1 into the register (result is true)
        compiler.addInstruction(new LOAD(1, Register.getR(compiler.getStack().getCurrentRegister() - 1)));

        // Add label for the end of the NOT operation
        compiler.addLabel(endNot);

    }
//    @Override
//    protected void codeGenPrint(DecacCompiler compiler) {
//        if(compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()){
//
//            // Create labels for the end of the NOT operation and the false condition
//            Label endNot = new Label("end_not");
//            Label falseNot = new Label("false_not");
//
//            // Generate code for the operand
//            super.getOperand().codeGenInst(compiler);
//
//            compiler.addInstruction(new LOAD(
//                    Register.getR(compiler.getStack().getCurrentRegister() - 1),
//                    Register.R1
//            ));
//
//            compiler.addInstruction(new CMP(0, Register.R1));
//
//            compiler.addInstruction(new BEQ(falseNot));
//
//            compiler.addInstruction(new WSTR("false"));
//            compiler.addInstruction(new BRA(endNot));
//
//            compiler.addLabel(falseNot);
//            compiler.addInstruction(new WSTR("true"));
//
//            compiler.addLabel(endNot);
//        }
//        else {
//            compiler.getStack().pushRegister(compiler);
//            codeGenPrint(compiler);
//            compiler.getStack().popRegister(compiler);
//
//        }
//
//
//
//
//    }

}
