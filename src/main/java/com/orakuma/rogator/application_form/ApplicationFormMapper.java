package com.orakuma.rogator.application_form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ApplicationFormMapper {

    Logger LOG = LoggerFactory.getLogger(ApplicationFormMapper.class);

    @Mapping(target = "application", ignore = true)
    @Mapping(target= "content", expression = "java(stringifyContent(applicationFormDto.content()))")
    ApplicationFormEntity toEntity(ApplicationFormDto applicationFormDto);
    @Mapping(target = "applicationId", source = "applicationFormEntity.application.id")
    @Mapping(target = "content", expression = "java(toMap(applicationFormEntity.getContent()))")
    ApplicationFormDto toDto(ApplicationFormEntity applicationFormEntity);
    List<ApplicationFormEntity> toEntities(List<ApplicationFormDto> applicationFormDtoList);
    List<ApplicationFormDto> toDtos(List<ApplicationFormEntity> applicationFormEntityList);

    default String stringifyContent(Map<String, Object> contentMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(contentMap);
        } catch (JsonProcessingException e) {
            LOG.error("Error serializing content to JSON: {}", e.getMessage());
            throw new RuntimeException("Error serializing content to JSON", e);
        }
    }

    default Map<String, Object> toMap(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> contentMap = objectMapper
                    .readValue(content, new TypeReference<Map<String, Object>>() {});
            return contentMap;
        } catch (JsonProcessingException e) {
            LOG.error("ReadValue content to Map has failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
