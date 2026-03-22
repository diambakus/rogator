package com.orakuma.rogator.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target= "status", expression = "java(toApplicationStatus(applicationDto.status()))")
    @Mapping(target = "created", ignore = true)
    ApplicationEntity toEntity(ApplicationDto applicationDto);
    @Mapping(target="created", expression = "java(toStringDate(applicationEntity.getCreated()))")
    ApplicationDto toDto(ApplicationEntity applicationEntity);
    List<ApplicationEntity> toEntities(List<ApplicationDto> applicationDtos);
    List<ApplicationDto> toDtos(List<ApplicationEntity> applicationEntities);

    default ApplicationStatus toApplicationStatus(String status) {
        return switch (status.toUpperCase()) {
            case "CREATED" -> ApplicationStatus.CREATED;
            case "ONGOING" -> ApplicationStatus.ONGOING;
            case "PROCESSING" -> ApplicationStatus.PROCESSING;
            case "PAID" -> ApplicationStatus.PAID;
            case "PAYMENT_FAILED" -> ApplicationStatus.PAYMENT_FAILED;
            case "APPROVED" -> ApplicationStatus.APPROVED;
            case "DONE" -> ApplicationStatus.DONE;
            default -> {
                yield ApplicationStatus.CANCELLED;
            }
        };
    }

    default String toStringDate(LocalDateTime date) {
        return date.toString();
    }
}
