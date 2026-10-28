package com.orakuma.rogator.payment.pispi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record CreatePaymentRequestResponse(String txId, String statut, String endToEndId) {}