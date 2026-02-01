package com.orakuma.rogator.file_upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    UploadedFileDto findById(Long id);
    List<UploadedFileDto> storeAll(
            Long applicationId,
            List<MultipartFile> files
    );
    List<UploadedFileDto> findByApplicationId(Long applicationId);
}
