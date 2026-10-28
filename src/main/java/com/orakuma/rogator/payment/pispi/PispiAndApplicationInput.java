package com.orakuma.rogator.payment.pispi;

import java.math.BigDecimal;

public record PispiAndApplicationInput(
    String applicationCode, BigDecimal price, String serviceTitle, String payeurAlias) {}
