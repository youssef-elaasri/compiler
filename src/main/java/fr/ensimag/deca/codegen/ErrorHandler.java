package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.util.HashMap;

public class ErrorHandler {
    HashMap<String, Label> errors;


    public ErrorHandler(){
        errors = new HashMap<>();
    }


    public Label addStackOverflowError(){
        Label label = new Label("stack_overflow_error");
        errors.putIfAbsent(
                "Error: Stack Overflow",
                label
        );
        return label;
    }

    public Label addDivisionByZero(){
        Label label = new Label("division_by_zero");
        errors.putIfAbsent(
                "Error: Division by Zero",
                label
                );
        return label;
    }

    public Label getLabel(String errorMessage){
        return errors.get((errorMessage));
    }

    /**
     * Generates the labels in assembly for raising errors
     * @param compiler the program's compiler
     */
    public void putErrors(DecacCompiler compiler){
        for(String errorMessage : errors.keySet()){
            compiler.addLabel(getLabel(errorMessage));
            compiler.addInstruction(new WSTR(new ImmediateString(errorMessage)));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }
}
