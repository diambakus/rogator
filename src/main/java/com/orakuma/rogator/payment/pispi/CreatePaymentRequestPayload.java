package com.orakuma.rogator.payment.pispi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatePaymentRequestPayload(
    String payeurAlias,
    BigDecimal montant,
    String motif,
    String txId,
    boolean confirmation,
    String categorie,
    String payeAlias,
    String logoUrl,
    LocalDateTime dateLimitePaiement) {}
