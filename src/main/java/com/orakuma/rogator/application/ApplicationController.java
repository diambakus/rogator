package com.orakuma.rogator.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "applications")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class ApplicationController {
  private final ApplicationService applicationService;

  @GetMapping(path = "/email/{email}")
  public List<ApplicationDto> getAllApplicationsByEmail(@PathVariable("email") String email) {
    return applicationService.findAllByEmail(email);
  }

  @GetMapping
  public List<ApplicationDto> getAllApplications(
      @RequestParam(required = false, name = "email") String email,
      @RequestParam(required = false, name = "status") String status) {
    if (!StringUtils.isBlank(status) && !StringUtils.isBlank(email)) {
      return applicationService.getAllByEmailAndStatus(email, status);
    } else if (StringUtils.isNotBlank(email)) {
      return applicationService.findAllByEmail(email);
    }
    return applicationService.getAll();
  }

  @GetMapping("/display-after-login/{employeeId}")
  public List<ApplicationDto> getInitialRelevantApplicationsForEmployee(
      @PathVariable("employeeId") String employeeId) {
    return applicationService.getAllRelevantApplications(employeeId);
  }

  @PostMapping
  public ApplicationDto createApplication(@RequestBody ApplicationDto applicationDto) {
    log.info("Creating application at date and time: {}", LocalDateTime.now());
    return applicationService.save(applicationDto);
  }

  @PatchMapping("/{publicId}")
  public ApplicationDto updateApplication(
      @PathVariable("publicId") Long publicId, @RequestBody Map<String, Object> fieldsValuesMap) {
    return applicationService.update(publicId, fieldsValuesMap);
  }

  @GetMapping("/{publicId}")
  public ApplicationDto getApplicationByPublicId(@PathVariable("publicId") String publicId) {
    return applicationService.getApplicationByPublicId(publicId);
  }

  @GetMapping("/internal/{id}")
  public ApplicationDto getApplicationById(@PathVariable("id") Long id) {
    return applicationService.findById(id);
  }

  @DeleteMapping("/{publicId}")
  public void deleteApplication(@PathVariable("publicId") String publicId) {
    applicationService.deleteByPublicId(publicId);
  }
}
