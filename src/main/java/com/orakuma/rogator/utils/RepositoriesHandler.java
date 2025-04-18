package com.orakuma.rogator.utils;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.application.ApplicationRepository;
import com.orakuma.rogator.application.exceptions.ApplicationNotFoundException;
import com.orakuma.rogator.application_form.ApplicationFormEntity;
import com.orakuma.rogator.application_form.ApplicationFormRepository;
import com.orakuma.rogator.application_form.exceptions.ApplicationFormNotFoundException;
import com.orakuma.rogator.file_upload.FileUploadEntity;
import com.orakuma.rogator.file_upload.FileUploadRepository;
import com.orakuma.rogator.file_upload.exceptions.FileUploadNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RepositoriesHandler {
    private final ApplicationRepository     applicationRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final FileUploadRepository      fileUploadRepository;

    public RepositoriesHandler(
            final ApplicationRepository applicationRepository,
            final ApplicationFormRepository applicationFormRepository,
            final FileUploadRepository fileUploadRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.applicationFormRepository = applicationFormRepository;
        this.fileUploadRepository = fileUploadRepository;
    }

    public ApplicationEntity getApplicationEntityById(Long id) {
        return applicationRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format("Application with id %d not found", id);
            return new ApplicationNotFoundException(errorMessage);
        });
    }

    public ApplicationEntity getApplicationEntityByName(String name) {
        ApplicationEntity applicationEntity = applicationRepository.findByName(name);
        if (applicationEntity == null) {
            String errorMessage = String.format("Application with name %s not found", name);
            throw new ApplicationNotFoundException(errorMessage);
        }

        return applicationEntity;
    }

    public ApplicationFormEntity getApplicationFormEntityById(Long id) {
        return applicationFormRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format("ApplicationForm with id %d not found", id);
            return new ApplicationFormNotFoundException(errorMessage);
        });
    }

    public FileUploadEntity getFileUploadEntityById(Long id) {
        return fileUploadRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format("FileUploadEntity with id %d not found", id);
            return new FileUploadNotFoundException(errorMessage);
        });
    }
}
