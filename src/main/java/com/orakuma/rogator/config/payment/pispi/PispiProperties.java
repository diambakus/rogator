package com.orakuma.rogator.config.payment.pispi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.pispi")
public record PispiProperties(
    String clientId,
    char[] clientSecret,
    String authenticationUrl,
    String baseUrl,
    String apiKey,
    String paymentUrl,
    String paymentStatus,
    String logoUrl,
    String categorie,
    String payeAlias,
    Long durationLimitPayment) {}
