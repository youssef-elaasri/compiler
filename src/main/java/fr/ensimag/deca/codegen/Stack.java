package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.util.HashMap;

/**
 *
 */

public class Stack {
    private static int addrCounter = 1;
    private int numberOfRegisters = 16;
    private int maxTSTO;
    private int currentRegister;

    /**
     * The Stack constructor
     * It sets register to 15 since we have 16 registers in total
     * maxTSTO is set to 1 for now because we need to at least have a pointer to object.class in the stack
     */
    public Stack(){
        maxTSTO = 1;
        currentRegister = 2;
    }

    /**
     * Increases {@code this.maxTSTO} by one
     */
    public void increaseTSTO(){
        maxTSTO++;
    }

    /**
     *
     * @return {@code this.maxTSTO}
     */
    public int getMaxTSTO() {
        return maxTSTO;
    }

    public int getAddrCounter() {
        return addrCounter;
    }

    public void increaseAddrCounter(){
        addrCounter++;
    }

    public void decreaseAddrCounter(){
        addrCounter--;
    }

    public int getCurrentRegister(){
        return currentRegister;
    }

    public void increaseRegister(){
        currentRegister++;
    }

    public void decreaseRegister(){
        currentRegister--;
    }

    public int getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public static void pushRegister(DecacCompiler compiler){
        compiler.getStack().decreaseRegister();
        compiler.addInstruction(new PUSH(Register.getR(compiler.getStack().getCurrentRegister())));
    }

    public static void popRegister(DecacCompiler compiler){
        compiler.addInstruction(new POP(Register.getR(compiler.getStack().getCurrentRegister())));
        compiler.getStack().increaseRegister();
    }

}
