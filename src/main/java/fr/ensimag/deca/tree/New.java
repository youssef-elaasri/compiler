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
    AbstractIdentifier classNanme;

    public New(AbstractIdentifier abstractIdentifier){
        super();
        classNanme = abstractIdentifier;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = this.classNanme.verifyExpr(compiler, localEnv, currentClass);
        if (!classType.isClass()){
            throw new ContextualError("The type " + classType + " is not a class type", this.getLocation());
        }
        return classType;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        classNanme.decompile(s);
        s.print("()");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    }


}
