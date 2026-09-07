package com.orakuma.rogator.config.payment.paypal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.paypal")
public record PaypalProperties(
        String mode,
        String clientId,
        String clientSecret,
        String currencyCode
) {}