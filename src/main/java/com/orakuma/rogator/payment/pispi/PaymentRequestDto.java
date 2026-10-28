package com.orakuma.rogator.payment.pispi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentRequestDto(
    String orderId, BigDecimal montant, String motif, String payeurAlias) {}
