package com.orakuma.rogator.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target= "status", expression = "java(toApplicationStatus(applicationDto.status()))")
    ApplicationEntity toEntity(ApplicationDto applicationDto);
    ApplicationDto toDto(ApplicationEntity applicationEntity);
    List<ApplicationEntity> toEntities(List<ApplicationDto> applicationDtos);
    List<ApplicationDto> toDtos(List<ApplicationEntity> applicationEntities);

    default ApplicationStatus toApplicationStatus(String status) {
        return switch (status.toUpperCase()) {
            case "CREATED" -> ApplicationStatus.CREATED;
            case "PENDING" -> ApplicationStatus.PENDING;
            case "DONE" -> ApplicationStatus.DONE;
            default -> {
                yield ApplicationStatus.CANCELLED;
            }
        };
    }
}
