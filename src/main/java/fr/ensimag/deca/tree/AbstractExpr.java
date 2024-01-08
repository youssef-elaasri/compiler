package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
//        if (getType() == null) {
//            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
//        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        Type currentType = this.verifyExpr(compiler, localEnv, currentClass);
        if (!(expectedType instanceof FloatType && currentType instanceof IntType)) {
            if (!(currentType.getClass().isAssignableFrom(expectedType.getClass()))) {
                throw new ContextualError("assign_compatible condition in rvalue no-terminal fails !: Trying to assign " + currentType + " to " + expectedType, this.getLocation());
            }
        }
        if (expectedType.isFloat() && currentType.isInt()) {
            AbstractExpr convF = new ConvFloat(this);
            convF.verifyExpr(compiler, localEnv, currentClass);
            return convF;
        }
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        Type typeC = this.verifyExpr(compiler, localEnv, currentClass);
        if (!typeC.isBoolean()) {
            throw new ContextualError("Condition in ifThenElse loop must be of type boolean: " + typeC + " was given !", this.getLocation());
        }
    }

    private int labelCounter = 0;

    private void increaseLabelCounter(){
        labelCounter++;
    }
    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        if(compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()){
            codeGenInst(compiler);
            compiler.addInstruction(new LOAD(
                    Register.getR(compiler.getStack().getCurrentRegister() - 1),
                    Register.R1
            ));

            if(this.getType().isFloat())
                compiler.addInstruction(new WFLOAT());

            else if (this.getType().isInt())
                compiler.addInstruction(new WINT());

            else{
                // Create labels for the end of the NOT operation and the false condition
                Label endNot = new Label("print_end_not_" + labelCounter);
                Label falseNot = new Label("print_false_not_"+ labelCounter);
                increaseLabelCounter();

                compiler.addInstruction(new CMP(0, Register.R1));

                compiler.addInstruction(new BEQ(falseNot));

                compiler.addInstruction(new WSTR("\"true\""));
                compiler.addInstruction(new BRA(endNot));

                compiler.addLabel(falseNot);
                compiler.addInstruction(new WSTR("\"false\""));

                compiler.addLabel(endNot);
            }
            compiler.getStack().decreaseRegister();
        }else {

            compiler.getStack().pushRegister(compiler);
            codeGenPrint(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {

    }


    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    /** ADDED CODE **/

    /**
     * Get the DVal (Data Value) representation for a given expression.
     * Determines the appropriate DVal representation based on the type of the expression.
     * Supported expression types include Identifiers, IntLiterals, FloatLiterals, and BooleanLiterals.
     *
     * @param expr The AbstractExpr for which to obtain the DVal representation.
     * @return The DVal representation of the given expression, or null if the expression type is not supported.
     */
    protected DVal getDval(AbstractExpr expr) {
        if (expr instanceof Identifier) {
            return ((Identifier) expr).getExpDefinition().getOperand();
        } else if (expr instanceof IntLiteral) {
            return new ImmediateInteger(((IntLiteral) expr).getValue());
        } else if (expr instanceof FloatLiteral) {
            return new ImmediateFloat(((FloatLiteral) expr).getValue());
        } else if (expr instanceof BooleanLiteral) {
            return new ImmediateInteger(((BooleanLiteral) expr).getValue() ? 1 : 0);
        }
        return null;
    }
}
