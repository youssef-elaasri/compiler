package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;

public class ErrorHandler {
    HashMap<String, Label> errors;

    public ErrorHandler(){
        errors = new HashMap<>();
    }


    public void addStackOverflowError(){
        errors.put(
                "Error: Stack Overflow",
                new Label("stack_overflow_error")
        );
    }

    public void addDivisionByZero(){
        errors.put(
                "Error: Division by Zero",
                new Label("division_by_zero")
                );
    }
}
