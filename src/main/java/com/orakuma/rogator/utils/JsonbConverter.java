package com.orakuma.rogator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter(autoApply = true)
public class JsonbConverter  implements AttributeConverter<Map<String, Object>, String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing content to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return OBJECT_MAPPER.readValue(dbData, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing content from JSON", e);
        }
    }
}
