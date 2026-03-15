package com.orakuma.rogator.application;

import java.math.BigDecimal;

public record ApplicationDto(
    Long id,
    String name,
    String email,
    BigDecimal price,
    String status,
    String created,
    Long requestedToUnitId,
    String assigneeId) {}
