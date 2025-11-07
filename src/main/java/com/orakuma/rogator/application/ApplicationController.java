package com.orakuma.rogator.application;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "application")
@AllArgsConstructor
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

    @PostMapping
    public ApplicationDto createApplication(@RequestBody ApplicationDto applicationDto) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(authentication);

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