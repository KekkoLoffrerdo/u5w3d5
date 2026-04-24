package com.example.u5w3d5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(UUID id) {
        super("La risorsa con id " + id + " non è stata trovata!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}