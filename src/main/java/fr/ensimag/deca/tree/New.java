package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr{
    AbstractIdentifier className;

    public New(AbstractIdentifier abstractIdentifier){
        super();
        className = abstractIdentifier;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = className.verifyType(compiler);
        if (!classType.isClass()){
            throw new ContextualError("The type " + classType + " is not a class type", this.getLocation());
        }
        this.setType(classType);
        return classType;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        className.decompile(s);
        s.print("()");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);

        
    }


//    @Override
//    protected void prettyPrintType(PrintStream s, String prefix) {
//        Type t = this.getType();
//        if (t != null) {
//            s.print(prefix);
//            s.print("type: ");
//            s.print(t);
//            s.println();
//        }
//
//    }



    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new NEW(
                compiler.getClassManager().get(className).getClassName().getClassDefinition().getNumberOfFields() + 1,
                Register.getR(compiler.getStack().getCurrentRegister())
        ));
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addFullStack()));
        compiler.addInstruction(
                new LEA(compiler.getClassManager().get(className).getClassName().getClassDefinition().getOperand(),
                Register.R0
                ));
        compiler.addInstruction(
                new STORE(Register.R0,new RegisterOffset(0,
                        Register.getR(compiler.getStack().getCurrentRegister()))
                ));
        compiler.addInstruction(new PUSH(
                Register.getR(compiler.getStack().getCurrentRegister()
        )));
        compiler.addInstruction(
                new BSR(new Label("init."
                        + compiler.getClassManager().get(className).getClassName().getName())
                ));
        compiler.addInstruction(new POP(
                Register.getR(compiler.getStack().getCurrentRegister()
                )));

        compiler.getStack().increaseRegister();
    }


}
