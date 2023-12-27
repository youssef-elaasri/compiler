package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;

/**
 *
 */

public class Stack {
    private static int addrCounter = 1;
    private static int numberOfRegisters = 2;
    private int maxTSTO;
    private int currentRegister;
    private HashMap<Symbol, Integer> symbolAddr;

    /**
     * The Stack constructor
     * It sets register to 15 since we have 16 registers in total
     * maxTSTO is set to 1 for now because we need to at least have a pointer to object.class in the stack
     */
    public Stack(){
        maxTSTO = 1;
        numberOfRegisters = 15;
        symbolAddr = new HashMap<>();

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
}
