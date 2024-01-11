package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {
    final private AbstractIdentifier className;
    final private AbstractIdentifier superName;
    final private ListDeclField listField;
    final private ListDeclMethod listMethod;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superName, ListDeclField listField, ListDeclMethod listMethod) {
//        Validate.notNull(className);
//        Validate.notNull(superName);
//        Validate.notNull(listField);
//        Validate.notNull(listMethod);
        this.className = className;
        this.superName = superName;
        this.listField = listField;
        this.listMethod = listMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        SymbolTable.Symbol superSymb = superName.getName();
        Definition superDef = compiler.environmentType.defOfType(superSymb);
        if (superDef == null) {
            throw new ContextualError("Super Class " + superSymb + " is not defined !", this.getLocation());
        }
        if (!superDef.isClass()) {
            throw new ContextualError("Identifier " + superSymb + " is not of type Class !", this.getLocation());
        }
        SymbolTable.Symbol classSymb = className.getName();
        if (compiler.environmentType.defOfType(classSymb) != null) {
            throw new ContextualError("Class " + classSymb + " is already defined !", this.getLocation());
        }
        compiler.environmentType.declareClass(className, (ClassDefinition) superDef);

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
//        throw new UnsupportedOperationException("Not yet supported");
        className.prettyPrint(s, prefix, false);
        superName.prettyPrint(s, prefix, false);
        listField.prettyPrint(s, prefix, false);
        listMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
//        throw new UnsupportedOperationException("Not yet supported");
        className.iter(f);
        superName.iter(f);
        listField.iter(f);
        listMethod.iter(f);
    }

}
