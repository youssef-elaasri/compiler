/* A manual test for the initial sketch of code generation included in 
 * students skeleton.
 * 
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class ManualTestInitialGencode {
    
    public static AbstractProgram initTest1() {
        SymbolTable symbolTable = new SymbolTable();
        ListInst linst = new ListInst();
        ListDeclVar listDeclVar = new ListDeclVar();
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(listDeclVar,linst));
        Identifier intIdentifier = new Identifier(symbolTable.create("int"));
        Identifier xIdentifier = new Identifier(symbolTable.create("x"));
        Identifier yIdentifier = new Identifier(symbolTable.create("y"));
        Initialization xinitialization = new Initialization(new IntLiteral(1));
        Initialization yinitialization = new Initialization(xIdentifier);
        intIdentifier.setDefinition(new TypeDefinition(new IntType(symbolTable.create("int")),new Location(1,1,"test.deca")));
        xIdentifier.setDefinition(new VariableDefinition(new IntType(symbolTable.create("int")),new Location(1,1,"test.deca")));
        yIdentifier.setDefinition(new VariableDefinition(new IntType(symbolTable.create("int")),new Location(1,1,"test.deca")));
        DeclVar xdeclVar = new DeclVar( intIdentifier, xIdentifier,xinitialization);
        DeclVar ydeclVar = new DeclVar( intIdentifier, yIdentifier,yinitialization);
        listDeclVar.add(xdeclVar);
        listDeclVar.add(ydeclVar);
        return source;
    }
    
    public static String gencodeSource(AbstractProgram source) {
        DecacCompiler compiler = new DecacCompiler(null,null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() {
        AbstractProgram source = initTest1();
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");        
        String result = gencodeSource(source);
        System.out.println(result);
        assert(result.equals(
                "; Main program\n" +
                "; Beginning of main function:\n" +
                "	WSTR \"Hello \"\n" +
                "	WSTR \"everybody !\"\n" +
                "	WNL\n" +
                "	HALT\n"));
    }    

        
        
    public static void main(String args[]) {
        test1();
    }
}
