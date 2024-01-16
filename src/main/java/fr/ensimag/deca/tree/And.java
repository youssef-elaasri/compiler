package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {


    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    /**
     * Generates code for a logical AND operation.
     * If both the left and right operands are true, the result is true.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationBool(compiler,true);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }



    @Override
    public int isOr() {
        return 0;
    }


}
