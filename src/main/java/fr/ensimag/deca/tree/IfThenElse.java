package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    private int counterIf = 0;

    private void increaseCounterIf(){
        counterIf++;
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            Label ifBranch = new Label("if_branch_" + counterIf);
            Label elseBranch_ = new Label("else_branch_" + counterIf);
            Label endOfIf = new Label("end_of_if_" + counterIf);

            increaseCounterIf();

            condition.codeGenInst(compiler);
            compiler.addInstruction(new CMP(
                    0,
                    Register.getR(compiler.getStack().getCurrentRegister() - 1)
            ));
            compiler.getStack().decreaseRegister();
            compiler.addInstruction(new BEQ(elseBranch_));

            compiler.addLabel(ifBranch);
            for (AbstractInst abstractInst : thenBranch.getList()) {
                abstractInst.codeGenInst(compiler);
            }
            compiler.addInstruction(new BRA(endOfIf));

            compiler.addLabel(elseBranch_);
            for (AbstractInst abstractInst : elseBranch.getList()) {
                abstractInst.codeGenInst(compiler);
            }
            compiler.addLabel(endOfIf);
        }else{
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);

        }




    }

    @Override
    public void decompile(IndentPrintStream s) {
       String st = "if";
       s.print(st);
       condition.decompile(s);
       s.println("{");
       thenBranch.decompile(s);
       s.println("} else {");
       elseBranch.decompile(s);
       s.println("}");


    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
