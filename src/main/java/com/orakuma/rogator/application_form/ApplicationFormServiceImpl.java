package com.orakuma.rogator.application_form;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.utils.RepositoriesHandler;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ApplicationFormServiceImpl implements ApplicationFormService {
    private final ApplicationFormRepository applicationFormRepository;
    private final ApplicationFormMapper applicationFormMapper;
    private final RepositoriesHandler repositoriesHandler;

    public ApplicationFormServiceImpl(
            ApplicationFormRepository applicationFormRepository,
            RepositoriesHandler repositoriesHandler
    ) {
        this.applicationFormRepository = applicationFormRepository;
        this.applicationFormMapper = Mappers.getMapper(ApplicationFormMapper.class);
        this.repositoriesHandler = repositoriesHandler;
    }

    @Override
    @Transactional
    public ApplicationFormDto update(Long formId, ApplicationFormDto applicationFormDto) {
        ApplicationFormEntity convertedEntity = applicationFormMapper.toEntity(applicationFormDto);
        ApplicationFormEntity entity = repositoriesHandler.getApplicationFormEntityById(formId);

        if (Objects.nonNull(entity)) {
            entity = applicationFormRepository.updateWithJsonb(convertedEntity, formId);
        }

        return applicationFormMapper.toDto(entity);
    }

    @Override
    public List<ApplicationFormDto> save(List<ApplicationFormDto> applicationsForms) {
        return List.of();
    }

    @Override
    @Transactional
    public ApplicationFormDto save(Long applicationId, ApplicationFormDto applicationFormDto) {
        ApplicationFormEntity applicationFormEntity = applicationFormMapper.toEntity(applicationFormDto);
        ApplicationEntity applicationEntity = repositoriesHandler.getApplicationEntityById(applicationId);
        applicationFormEntity.setApplication(applicationEntity);
        applicationFormEntity.setCreated(LocalDate.now());
        ApplicationFormEntity persistedForm = applicationFormRepository.saveWithJsonb(applicationFormEntity, applicationId);
        return applicationFormMapper.toDto(persistedForm);
    }

    @Override
    public ApplicationFormDto getApplicationForm(Long formId) {
        ApplicationFormEntity formEntity = repositoriesHandler.getApplicationFormEntityById(formId);
        return applicationFormMapper.toDto(formEntity);
    }

    @Override
    @Transactional
    public void deleteApplicationForm(Long formId) {
        ApplicationFormEntity formEntity = repositoriesHandler.getApplicationFormEntityById(formId);
        applicationFormRepository.delete(formEntity);
    }

    @Override
    public boolean delete(ApplicationFormDto applicationFormDto) {
        return false;
    }

    @Override
    public List<ApplicationFormDto> findByApplicationId(Long applicationId) {
        List<ApplicationFormEntity> formEntities = applicationFormRepository.findAllByApplicationId(applicationId);
        return applicationFormMapper.toDtos(formEntities);
    }
}