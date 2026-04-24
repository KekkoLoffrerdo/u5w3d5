package com.example.u5w3d5.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errors;
    public ValidationException(List<String> errors) {
        super("Errori di validazione");
        this.errors = errors;
    }
    public List<String> getErrors() {
        return errors;
    }
}
