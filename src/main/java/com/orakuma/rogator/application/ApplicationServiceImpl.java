package com.orakuma.rogator.application;

import com.orakuma.rogator.utils.RepositoriesHandler;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper     applicationMapper;
    private final RepositoriesHandler   repositoriesHandler;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, RepositoriesHandler repositoriesHandler) {
        this.applicationRepository = applicationRepository;
        this.repositoriesHandler = repositoriesHandler;
        this.applicationMapper = Mappers.getMapper(ApplicationMapper.class);
    }

    @Override
    public ApplicationDto save(ApplicationDto applicationDto) {
        ApplicationEntity applicationEntity = applicationMapper.toEntity(applicationDto);
        applicationEntity.setCreated(LocalDateTime.now());
        applicationEntity = applicationRepository.save(applicationEntity);
        return applicationMapper.toDto(applicationEntity);
    }

    @Override
    public ApplicationDto update(Long id, Map<String, Object> fieldsAndValuesMap) {
        ApplicationEntity applicationEntity = repositoriesHandler.getApplicationEntityById(id);

        fieldsAndValuesMap.forEach((key, value) -> {
            if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "price")) {
               applicationEntity.setPrice((BigDecimal) value);
            } else if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "status")) {
                applicationEntity.setStatus(ApplicationStatus.valueOf(value.toString()));
            }
        });
        ApplicationEntity persisted = applicationRepository.save(applicationEntity);
        return applicationMapper.toDto(persisted);
    }

    @Override
    public ApplicationDto findById(long id) {
        ApplicationEntity applicationEntity = repositoriesHandler.getApplicationEntityById(id);
        return applicationMapper.toDto(applicationEntity);
    }

    @Override
    public ApplicationDto findByName(String name) {
        ApplicationEntity applicationEntity = repositoriesHandler.getApplicationEntityByName(name);
        return applicationMapper.toDto(applicationEntity);
    }

    @Override
    public List<ApplicationDto> findAllByEmail(String email) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findByEmail(email);
        return applicationMapper.toDtos(applicationEntities);
    }

    @Override
    public List<ApplicationDto> getAllByEmailAndStatus(String email, String status) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findByEmailAndStatus(email, status);
        return applicationMapper.toDtos(applicationEntities);
    }

    @Override
    public void deleteById(long id) {
        applicationRepository.deleteById(id);
    }
}
