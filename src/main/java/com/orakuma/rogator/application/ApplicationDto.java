package com.orakuma.rogator.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationDto(
        Long          id,
        String        name,
        String        email,
        BigDecimal    price,
        String        status,
        LocalDateTime created,
        Long requestedToUnitId
) {
}