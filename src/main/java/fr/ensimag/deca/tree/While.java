package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    private static int counter = 0;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for a while loop, including the condition evaluation and loop body.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            int i = counter;
            increaseCounter();
            Label whileLabel = new Label("while_" + i);
            Label endOfWhile = new Label("end_of_while_" + i);
            compiler.addLabel(whileLabel);
            condition.codeGenInst(compiler);
            compiler.addInstruction(new CMP(0, Register.getR(compiler.getStack().getCurrentRegister()-1)));
            compiler.addInstruction(new BEQ(endOfWhile));
            compiler.getStack().decreaseRegister();
            for (AbstractInst inst : body.getList()) {
                inst.codeGenInst(compiler);
            }
            compiler.addInstruction(new BRA(whileLabel));
            compiler.addLabel(endOfWhile);
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    private void increaseCounter() {
        counter++;
    }
}
