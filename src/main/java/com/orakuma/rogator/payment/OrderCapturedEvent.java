package com.orakuma.rogator.payment;

import java.math.BigDecimal;

public record OrderCapturedEvent(
        String requestingServiceTitle,
        String trackingCode,
        BigDecimal price,
        String requestorEmail
) {}
