package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.*;

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
        superName.setDefinition(compiler.environmentType.defOfType(superSymb));
        className.setDefinition(compiler.environmentType.defOfType(className.getName()));
        LOG.debug("verify verifyClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify verifyClassMembers: start");
        EnvironmentExp envExpf = listField.verifyListDeclField(compiler, superName, className);
        EnvironmentExp envExpm = listMethod.verifyListDeclMethod(compiler, superName, className);
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
        /*Here we build the classDefinition of our currentClass based on the updated ClassDefinition of our superClass*/
        superName.setDefinition(compiler.environmentType.defOfType(superName.getName()));
        ClassType classType = new ClassType(className.getName(), className.getLocation(), superName.getClassDefinition());
        ClassDefinition classDef = new ClassDefinition(classType, className.getLocation(), superName.getClassDefinition());
        /*Here we set the members of our currentClass to complete the definition*/
        classDef.getMembers().setExpDefinitionMap(mergedMap);
        /*Here we set the number of fields and methods based on the previous definition of our class */
        classDef.setNumberOfFields(className.getClassDefinition().getNumberOfFields());
        classDef.setNumberOfMethods(className.getClassDefinition().getNumberOfMethods());
        compiler.environmentType.put(className.getName(), classDef);
        className.setDefinition(classDef);
        LOG.debug("verify verifyClassMembers: end");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        /*The two first passes ensure that className is defined in environmentType as a class)*/
        EnvironmentExp envExp = ((ClassDefinition) compiler.environmentType.defOfType(className.getName())).getMembers();
        listField.verifyListDeclFieldI(compiler, envExp, className.getClassDefinition());
       listMethod.verifyListDeclMethodBody(compiler, envExp, className.getClassDefinition());
    }

    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
        compiler.getClassManager().put(className,this);
        className.getDefinition().setOperand(new RegisterOffset(compiler.getStack().getAddrCounter(), Register.GB));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();

        boolean isSuperClassObject = superName.getName().equals(compiler.createSymbol("Object"));
        // define the supper class
        if (isSuperClassObject)
            compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB),Register.R0));

        else
            compiler.addInstruction(new LEA(superName.getDefinition().getOperand(),Register.R0));

        compiler.addInstruction(new STORE(Register.R0,className.getDefinition().getOperand()));

        // define methods

//        int  nbrOfMethods = listMethod.getNbrOfAllMethods(compiler.getClassManager().get(superName));

        Map<Integer, Label> methodTable = new HashMap<>();

        if (!isSuperClassObject){
            for (AbstractDeclMethod method : compiler.getClassManager().get(superName).listMethod.getList()) {
                int index = method.getIndex();
                methodTable.put(index, new Label("code." + superName.getName().toString() + "." + method.getMethodName().getName().toString()));
            }
        }

        for (AbstractDeclMethod method : compiler.getClassManager().get(className).listMethod.getList()){
            int index = method.getIndex();
            methodTable.put(index, new Label("code." + className.getName().toString() + "." + method.getMethodName().getName().toString()));
        }

        Program.setOperandEquals(compiler);
        for(Label methodLabel : methodTable.values()){
            Program.setOperandMethod(compiler,methodLabel);
        }


    }

    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        compiler.addComment("Initialize " + this.className.getName() + "'s fields");
        Label init = new Label("init." + this.className.getName());
        compiler.addLabel(init);
        if (superName.getName().toString().equals("Object")) {
//            int offset = 1;
            for (AbstractDeclField abstractDeclField : listField.getList()) {
                int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                if (((DeclField) abstractDeclField).getInitialization() instanceof NoInitialization) {
                    compiler.addInstruction(new LOAD(0, Register.R0));
                    compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
                    compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
                } else {
                    codeGenInit(compiler, abstractDeclField, index);
                }
                compiler.getStack().resetCurrentRegister();
//                offset++;
            }
        }
        else {
            ImmediateInteger TSTOimmediateInteger = new ImmediateInteger(className.getClassDefinition().getNumberOfFields() - superName.getClassDefinition().getNumberOfFields() + 1);
            compiler.addInstruction(new TSTO(TSTOimmediateInteger));

            if (!compiler.getCompilerOptions().getNoCheck())
                compiler.addInstruction(new BOV(compiler.getErrorHandler().addStackOverflowError()));

            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));

            compiler.addInstruction(new LOAD(0, Register.R0));
            for (AbstractDeclField abstractDeclField : listField.getList()) {
                int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
            }
            compiler.getStack().pushRegister(compiler, Register.R1);
            Label initBSR = new Label("init." + this.superName.getName());
            compiler.addInstruction(new BSR(initBSR));
            compiler.addInstruction(new SUBSP(1));
            for (AbstractDeclField abstractDeclField : listField.getList()) {
                if ((((DeclField) abstractDeclField).getInitialization() instanceof Initialization)) {
                    int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                    codeGenInit(compiler, abstractDeclField, index);
                }
            }
            compiler.getStack().resetCurrentRegister();
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

    public ListDeclMethod getListMethodSize(){
        return listMethod;
    }

    public void codeGenInit(DecacCompiler compiler, AbstractDeclField abstractDeclField, int index) {
        compiler.getStack().setCurrentRegister(0);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        ((Initialization) ((DeclField) abstractDeclField).getInitialization()).getExpression().codeGenInst(compiler);
        compiler.addInstruction(new STORE(Register.getR(compiler.getStack().getCurrentRegister() - 1)
                , new RegisterOffset(index, Register.R1)));
    }
}
