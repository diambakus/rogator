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
@RequestMapping(path = "application")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping(path = "/id/{id}")
    public ApplicationDto getApplication(@PathVariable("id") Long id) {
        return applicationService.findById(id);
    }

    @GetMapping(path = "/name/{name}")
    public ApplicationDto getApplicationByName(@PathVariable("name") String name) {
        return applicationService.findByName(name);
    }

    @GetMapping(path = "/email/{email}")
    public List<ApplicationDto> getAllApplicationsByEmail(@PathVariable("email") String email) {
        return applicationService.findAllByEmail(email);
    }

    @GetMapping
    public List<ApplicationDto> getAllApplications(
            @RequestParam(required = false, name = "email") String email,
            @RequestParam(required = false, name = "status") String status
    ) {
        if (!StringUtils.isBlank(status) && !StringUtils.isBlank(email)) {
            return applicationService.getAllByEmailAndStatus(email, status);
        } else if (StringUtils.isNotBlank(email)) {
            return applicationService.findAllByEmail(email);
        }
        return applicationService.getAll();
    }

    @GetMapping("/display-after-login/{employeeId}")
    public List<ApplicationDto> getInitialRelevantApplicationsForEmployee(@PathVariable("employeeId") String employeeId) {
        return applicationService.getAllRelevantApplications(employeeId);
    }

    @PostMapping
    public ApplicationDto createApplication(@RequestBody ApplicationDto applicationDto) {
        log.info("Creating application at date and time: {}", LocalDateTime.now());
        return applicationService.save(applicationDto);
    }

    @PatchMapping("/{id}")
    public ApplicationDto updateApplication(@PathVariable("id") Long id, @RequestBody Map<String, Object> fieldsValuesMap) {
        return applicationService.update(id, fieldsValuesMap);
    }

    @DeleteMapping
    public void deleteApplication(@PathVariable("id") Long id) {
        applicationService.deleteById(id);
    }
}