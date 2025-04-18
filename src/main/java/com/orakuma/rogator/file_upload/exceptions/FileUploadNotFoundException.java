package com.orakuma.rogator.file_upload.exceptions;

public class FileUploadNotFoundException extends RuntimeException {

    public FileUploadNotFoundException() {
    }

    public FileUploadNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
