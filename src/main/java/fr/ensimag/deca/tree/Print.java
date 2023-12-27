package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Print extends AbstractPrint {
    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printx)
     */
    public Print(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        String st = this.getPrintHex() ? "printx(" : "print(";
        s.print(st);
        this.getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
    }

    @Override
    String getSuffix() {
        return "";
    }
}
