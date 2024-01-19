package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractExpr exp : super.getList()){
            exp.decompile(s);
        }
    }

    public void verifyListRValues(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Signature sig) throws ContextualError{
        int paramCounter = 0;
        for( AbstractExpr rvalue : this.getList()){

            Type rvalueType = rvalue.verifyExpr(compiler, localEnv, currentClass);
            Type requiredType = sig.paramNumber(paramCounter);
            if (!rvalueType.sameType(requiredType)){
                throw new ContextualError("The type of " + rvalue.getType() +
                        " is not compatible with the method signature type: " + requiredType, this.getLocation());
            }
            paramCounter++;
        }
    }

}