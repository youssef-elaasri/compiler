package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        String opName = this.getOperatorName();
        switch (opName) {
            case "%":
                Type typeMod =  this.verifyExpr(compiler, localEnv, currentClass);
                this.setType(typeMod);
                return typeMod;
            default:
                AbstractExpr left = this.getLeftOperand();
                AbstractExpr right = this.getRightOperand();

                Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
                Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
                Type syntType = verifyArithOp(compiler, opName, type1, type2, localEnv, currentClass);
                this.setType(syntType);
                return syntType;
        }
    }

    public Type verifyArithOp(DecacCompiler compiler, String op, Type type1, Type type2, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if ((type1.isInt()) && (type2.isInt())) {
            return compiler.environmentType.INT;
        }
        if ((type1.isInt()) && (type2.isFloat())) {
            AbstractExpr leftOp = new ConvFloat(this.getLeftOperand());
            this.setLeftOperand(leftOp);
            leftOp.verifyExpr(compiler, localEnv, currentClass);
            return compiler.environmentType.FLOAT;
        }
        if ((type1.isFloat()) && (type2.isInt())) {
            AbstractExpr rightOp = new ConvFloat(this.getRightOperand());
            this.setRightOperand(rightOp);
            rightOp.verifyExpr(compiler, localEnv, currentClass);
            return compiler.environmentType.FLOAT;
        }
        if ((type1.isFloat()) && (type2.isFloat())) {
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError(op + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
    }

}