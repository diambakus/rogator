package com.orakuma.rogator.file_upload;

public interface FileUploadService {
    FileUploadDto upload(FileUploadDto fileUploadDto);
    boolean delete(Long fileId);
}
