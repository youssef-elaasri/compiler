package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superName, ListDeclField listField, ListDeclMethod listMethod) {
        Validate.notNull(className);
        Validate.notNull(superName);
        Validate.notNull(listField);
        Validate.notNull(listMethod);
        this.className = className;
        this.superName = superName;
        this.listField = listField;
        this.listMethod = listMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class");
        className.decompile(s);
        s.print("extends");
        superName.decompile(s);
        s.print(" {");
        this.listField.decompile(s);
        this.listMethod.decompile(s);
        s.print("}");



    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify verifyClass: start");
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
        className.setDefinition(compiler.environmentType.defOfType(classSymb));
        superName.setDefinition(compiler.environmentType.defOfType(superSymb));
        LOG.debug("verify verifyClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify verifyClassMembers: start");
        ClassType classType = new ClassType(className.getName(), className.getLocation(), superName.getClassDefinition());
        ClassDefinition classDef = new ClassDefinition(classType, className.getLocation(), superName.getClassDefinition());
        classDef.setNumberOfFields(listField.size());
        classDef.setNumberOfMethods(listMethod.size());

        EnvironmentExp envExpf = listField.verifyListDeclField(compiler, superName, className);
        EnvironmentExp envExpm = listMethod.verifyListDeclMethod(compiler, superName);

        Set<SymbolTable.Symbol> keyF = envExpf.getExpDefinitionMap().keySet();
        Set<SymbolTable.Symbol> keyM = envExpm.getExpDefinitionMap().keySet();

        /*Vérifier si les deux environnements sont disjoints*/
        if (!Collections.disjoint(keyF, keyM)) {
            throw new ContextualError("Un champ et une méthode ont le même nom dans la classe " + className.getName() + " !", this.getLocation());
        }
        /*Method putAll overwrites Symbols already present in the superclass map*/
        Map<SymbolTable.Symbol, ExpDefinition> mergedMap = new HashMap<>(superName.getClassDefinition().getMembers().getExpDefinitionMap());
        mergedMap.putAll(envExpm.getExpDefinitionMap());
        mergedMap.putAll(envExpf.getExpDefinitionMap());

        classDef.getMembers().setExpDefinitionMap(mergedMap);

        compiler.environmentType.put(className.getName(), classDef);
        LOG.debug("verify verifyClassMembers: end");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        /*The two first passes ensure that className is defined in environmentType as a class)*/
        EnvironmentExp envExp = ((ClassDefinition) compiler.environmentType.defOfType(className.getName())).getMembers();
        listField.verifyListDeclFieldI(compiler, envExp, className.getClassDefinition());
//        listMethod.verifyListDeclMethodBody(compiler, envExp, className.getClassDefinition());
    }

    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
        compiler.getClassManager().put(className,this);
        className.getDefinition().setOperand(new RegisterOffset(compiler.getStack().getAddrCounter(), Register.GB));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();

        // define the supper class
        if (superName.getName().equals(compiler.createSymbol("Object")))
            compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB),Register.R0));

        else
            compiler.addInstruction(new LEA(superName.getDefinition().getOperand(),Register.R0));

        compiler.addInstruction(new STORE(Register.R0,className.getDefinition().getOperand()));

        // define methods

        Program.setOperandEquals(compiler);
        for(AbstractDeclMethod method : this.listMethod.getList()){

            Label codeMethodLabel = new Label("code." + className.getName().toString() + "." + method.getMethodName().getName().toString());
            Program.setOperandMethod(compiler,codeMethodLabel);
        }


    }

    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        compiler.addComment("Initialize " + this.className.getName() + "'s fields");

        Label init = new Label("init." + this.className.getName());
        compiler.addLabel(init);
        compiler.addInstruction(new LOAD(0, Register.R0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.R1));
        int offset = 1;
        for(AbstractDeclField abstractDeclField : listField.getList()){
            ((DeclField) abstractDeclField).setOffset(offset);
            if (((DeclField) abstractDeclField).getInitialization() instanceof NoInitialization) {
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(offset,Register.R1)));
            } else {
                ((Initialization) ((DeclField) abstractDeclField).getInitialization()).getExpression().codeGenInst(compiler);
                compiler.addInstruction(new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1)
                        ,new RegisterOffset(offset,Register.R1)));
            }
            offset++;
        }
        compiler.addInstruction(new RTS());

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

    public AbstractIdentifier getClassName() {
        return className;
    }

    public ListDeclField getListField() {
        return listField;
    }
}
