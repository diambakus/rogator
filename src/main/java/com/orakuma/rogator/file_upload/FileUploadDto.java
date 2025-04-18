package com.orakuma.rogator.file_upload;

import java.time.LocalDate;

public record FileUploadDto(
        Long id,
        String fileName,
        String fileType,
        Long fileSize,
        String filePath,
        Long applicationId,
        LocalDate created
) {
}

