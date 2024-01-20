package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Return extends AbstractInst {
    private AbstractExpr returnedExpr;

    public Return(AbstractExpr exp){
        this.returnedExpr=exp;
    }
    public void setExpression(AbstractExpr returnedExpr) {
        this.returnedExpr = returnedExpr;
    }
    
       protected  void verifyInst(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError{
            if(returnType.isVoid()){
                throw new ContextualError("Return Type should not be void !", this.getLocation());
            }
            AbstractExpr expression = returnedExpr.verifyRValue(compiler, localEnv, currentClass, returnType);
            this.setExpression(expression);
        }

    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     */
    protected  void codeGenInst(DecacCompiler compiler){

    }


    /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon if needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returnedExpr.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnedExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnedExpr.prettyPrint(s, prefix,true);
    }
}
