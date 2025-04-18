package com.orakuma.rogator.application.exceptions;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException() {}

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
