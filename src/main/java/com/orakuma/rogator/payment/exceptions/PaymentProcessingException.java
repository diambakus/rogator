package com.orakuma.rogator.payment.exceptions;

public class PaymentProcessingException extends RuntimeException {
  public PaymentProcessingException(String message) {
    super(message);
  }
}
