package com.orakuma.rogator.application_form.exceptions;

public class ApplicationFormNotFoundException extends RuntimeException {

    public ApplicationFormNotFoundException() {
    }

    public ApplicationFormNotFoundException(String message) {
        super(message);
    }
}
