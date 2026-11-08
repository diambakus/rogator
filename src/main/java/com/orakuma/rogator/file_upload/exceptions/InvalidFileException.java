package com.orakuma.rogator.file_upload.exceptions;

/** Rejected upload. {@code unsupportedType} distinguishes 415 from 400. */
public class InvalidFileException extends RuntimeException {
  private final boolean unsupportedType;

  public InvalidFileException(String message, boolean unsupportedType) {
    super(message);
    this.unsupportedType = unsupportedType;
  }

  public boolean isUnsupportedType() {
    return unsupportedType;
  }
}
