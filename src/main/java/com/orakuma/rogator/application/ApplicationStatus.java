package com.orakuma.rogator.application;

public enum ApplicationStatus {
  CANCELLED, // canceled manually
  CREATED, // user select a service
  ONGOING, // filling stepper
  PAID,
  PAYMENT_FAILED,
  PROCESSING, // backend(in employee hands) handling it
  APPROVED,
  DONE,
  ABANDONED
}
