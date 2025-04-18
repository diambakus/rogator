package com.orakuma.rogator.application;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(path = "/{id}")
    public ApplicationDto getApplication(@PathVariable("id") Long id) {
        return applicationService.findById(id);
    }

    @GetMapping(path = "/{name}")
    public ApplicationDto getApplicationByName(@PathVariable("name") String name) {
        return applicationService.findByName(name);
    }

    @GetMapping(path = "/{email}")
    public List<ApplicationDto> getAllApplicationsByEmail(@PathVariable("email") String email) {
        return applicationService.findAllByEmail(email);
    }

    @GetMapping
    public List<ApplicationDto> getAllApplicationsByEmailAndStatus(
            @RequestParam(name = "email") String email,
            @RequestParam(required = false, name = "status") String status
    ) {
        if (StringUtils.isBlank(status)) {
            return applicationService.findAllByEmail(email);
        }
        return applicationService.getAllByEmailAndStatus(email, status);
    }

    @PostMapping
    public ApplicationDto createApplication(@RequestBody ApplicationDto applicationDto) {
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
