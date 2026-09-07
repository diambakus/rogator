package com.orakuma.rogator.application;

import com.orakuma.rogator.utils.RepositoriesHandler;
import com.orakuma.rogator.utils.TrackingCodeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final ApplicationRepository applicationRepository;
  private final ApplicationMapper applicationMapper;
  private final RepositoriesHandler repositoriesHandler;

  @Override
  @Transactional
  public ApplicationDto save(ApplicationDto applicationDto) {
    ApplicationEntity applicationEntity = applicationMapper.toEntity(applicationDto);
    applicationEntity.setPublicId(String.format("app_%s", UUID.randomUUID()));
    applicationEntity.setCreated(LocalDateTime.now());
    applicationEntity.setTrackingCode(TrackingCodeUtils.generate(6));
    applicationEntity.setStatus(ApplicationStatus.CREATED);
    applicationEntity = applicationRepository.save(applicationEntity);
    return applicationMapper.toDto(applicationEntity);
  }

  @Override
  @Transactional
  public ApplicationDto update(Long id, Map<String, Object> fieldsAndValuesMap) {
    ApplicationEntity applicationEntity = repositoriesHandler.getApplicationEntityById(id);

    fieldsAndValuesMap.forEach(
        (key, value) -> {
          if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "price")) {
            applicationEntity.setPrice((BigDecimal) value);
          } else if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "status")) {
            applicationEntity.setStatus(ApplicationStatus.valueOf(value.toString()));
          } else if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "requestedToUnitId")) {
            applicationEntity.setRequestedToUnitId((Long) value);
          } else if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "assigneeId")) {
            applicationEntity.setAssigneeId(String.valueOf(value));
          } else if (StringUtils.isNotBlank(key) && StringUtils.equals(key, "serviceId")) {
            applicationEntity.setServiceId((Long) value);
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
  public List<ApplicationDto> findAllByEmail(String email) {
    List<ApplicationEntity> applicationEntities = applicationRepository.findByEmail(email);
    return applicationMapper.toDtos(applicationEntities);
  }

  @Override
  public List<ApplicationDto> getAllByEmailAndStatus(String email, String status) {
    List<ApplicationEntity> applicationEntities =
        applicationRepository.findByEmailAndStatus(email, status);
    return applicationMapper.toDtos(applicationEntities);
  }

  @Override
  @Transactional
  public void deleteById(long id) {
    applicationRepository.deleteById(id);
  }

  @Override
  public List<ApplicationDto> getAll() {
    List<ApplicationEntity> applicationEntities =
        StreamSupport.stream(applicationRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    return applicationMapper.toDtos(applicationEntities);
  }

  @Override
  public List<ApplicationDto> getAllRelevantApplications(String employeeId, Long unitId) {
    List<ApplicationEntity> applicationEntities =
        applicationRepository.findRelevantApplications(
            employeeId, unitId, ApplicationStatus.PROCESSING, ApplicationStatus.CREATED);
    return applicationMapper.toDtos(applicationEntities);
  }

  @Override
  public ApplicationDto getApplicationByPublicId(String publicId) {
    ApplicationEntity applicationEntity = applicationRepository
            .findByPublicId(publicId)
            .orElseThrow(()-> new EntityNotFoundException("Application not found for ID:" + publicId));
    return applicationMapper.toDto(applicationEntity);
  }

  @Override
  @Transactional
  public void deleteByPublicId(String publicId) {
    applicationRepository.deleteByPublicId(publicId);
  }

  @Override
  public List<ApplicationDto> getApplicationsForRequestorTrack(String requestorId) {
    List<ApplicationStatus> statuses =
        List.of(
            ApplicationStatus.CREATED,
            ApplicationStatus.PROCESSING,
            ApplicationStatus.ONGOING,
            ApplicationStatus.PAID,
            ApplicationStatus.PAYMENT_FAILED,
            ApplicationStatus.APPROVED);
    return applicationMapper.toDtos(
        applicationRepository.findByRequestorIdAndStatus(requestorId, statuses));
  }
}
