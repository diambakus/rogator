package com.orakuma.rogator.file_upload;

public record UploadedFileDto(
        Long id,
        String filename,
        String filePath,
        Long applicationId
) {
    public UploadedFileDto(String url) {
        this(null, null, url, null);
    }
}

