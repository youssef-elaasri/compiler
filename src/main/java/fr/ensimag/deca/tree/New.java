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
        Type classType = this.className.verifyExpr(compiler, localEnv, currentClass);
        if (!classType.isClass()){
            throw new ContextualError("The type " + classType + " is not a class type", this.getLocation());
        }
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


    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = className.getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }

    }



    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    }


}
