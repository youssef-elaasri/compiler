package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        return compiler.environmentType.FLOAT;
    }



    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    /** ADDED CODE **/

    /**
     * Generates code for the conversion of an operand to a floating-point value.
     * If the operand is a constant, the conversion is performed directly.
     * If the operand is not a constant, its value is loaded into a register and then converted.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        DVal dVal = getDval(getOperand());
        // Check if the operand is a constant (DVal)
        // If so then convert the constant directly to a floating-point value
        if (dVal != null) {
            compiler.addInstruction(new FLOAT(
                            dVal,
                            Register.getR(compiler.getStack().getCurrentRegister())
                    ));
            compiler.getStack().increaseRegister();
        }
        else {
            // Operand is not a constant, generate code for the operand
            super.getOperand().codeGenInst(compiler);
            // Load the value from the operand into a register and then convert to a floating-point value
            compiler.addInstruction(new FLOAT(
                    Register.getR(compiler.getStack().getCurrentRegister() - 1),
                    Register.getR(compiler.getStack().getCurrentRegister() - 1)
            ));
        }
    }

}
