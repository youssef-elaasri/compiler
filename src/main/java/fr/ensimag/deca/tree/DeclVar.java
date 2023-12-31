package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
//        if (type.getDefinition().getType() instanceof VoidType) {
//            throw new ContextualError("Type of variable must not be void", type.getDefinition().getLocation());
//        }
//        SymbolTable.Symbol symb = varName.getName();
//        ExpDefinition def = new VariableDefinition(type.getDefinition().getType(), type.getDefinition().getLocation());
//        localEnv.declare(symb, def);

        SymbolTable.Symbol name = varName.getName();
        Map<SymbolTable.Symbol, ExpDefinition> expDef = localEnv.getExpDefinitionMap();
        if (expDef.containsKey(name)) {
            throw new ContextualError("Name " + name + "is already defined in localEnv !", Location.BUILTIN);
        }

        Type ty = type.verifyType(compiler);
        initialization.verifyInitialization(compiler, ty, localEnv, currentClass);
        if (ty instanceof VoidType) {
            throw new ContextualError("Type of variable must not be void", type.getDefinition().getLocation());
        }
        VariableDefinition varDef = new VariableDefinition(ty, Location.BUILTIN);
        varName.setDefinition(varDef);

        expDef.put(name, varDef);
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
