package com.orakuma.rogator.file_upload;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {
    @Mapping(target = "application", ignore = true)
    FileUploadEntity toEntity(FileUploadDto fileUploadDto);
    @Mapping(target="applicationId", source = "fileUploadEntity.application.id")
    FileUploadDto toDto(FileUploadEntity fileUploadEntity);
    List<FileUploadEntity> toEntities(List<FileUploadDto> fileUploadDtos);
    List<FileUploadDto> toDtos(List<FileUploadEntity> fileUploadEntities);
}
