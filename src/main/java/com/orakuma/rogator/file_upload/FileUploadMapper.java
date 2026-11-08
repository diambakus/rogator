package com.orakuma.rogator.file_upload;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "storedFilename", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "created", ignore = true)
    FileUploadEntity toEntity(UploadedFileDto uploadedFileDto);
    @Mapping(target="applicationId", source = "fileUploadEntity.application.id")
    UploadedFileDto toDto(FileUploadEntity fileUploadEntity);
    List<FileUploadEntity> toEntities(List<UploadedFileDto> uploadedFileDtos);
    List<UploadedFileDto> toDtos(List<FileUploadEntity> fileUploadEntities);
}
