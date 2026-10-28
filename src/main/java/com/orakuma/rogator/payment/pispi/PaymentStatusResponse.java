package com.orakuma.rogator.payment.pispi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentStatusResponse(String statut, String invoiceUrl) {}
