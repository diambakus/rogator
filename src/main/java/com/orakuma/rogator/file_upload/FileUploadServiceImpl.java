package com.orakuma.rogator.file_upload;

import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public FileUploadDto upload(FileUploadDto fileUploadDto) {
        return null;
    }

    @Override
    public boolean delete(Long fileId) {
        return false;
    }
}
