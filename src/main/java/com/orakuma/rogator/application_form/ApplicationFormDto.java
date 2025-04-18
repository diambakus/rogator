package com.orakuma.rogator.application_form;

import java.time.LocalDate;
import java.util.Map;

public record ApplicationFormDto(
        Long id,
        int position,
        String name,
        Long applicationId,
        Map<String, Object> content,
        LocalDate created
) {
}