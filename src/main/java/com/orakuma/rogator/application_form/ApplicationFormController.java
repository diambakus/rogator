package com.orakuma.rogator.application_form;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "application-form")
@AllArgsConstructor
public class ApplicationFormController {
    private final ApplicationFormService applicationFormService;

    @GetMapping(path = "/{formId}")
    public ApplicationFormDto getApplicationForm(@PathVariable("formId") Long formId) {
        return applicationFormService.getApplicationForm(formId);
    }

    @GetMapping(path = "/{applicationId}/{position}")
    public ApplicationFormDto getApplicationForm(
            @PathVariable("applicationId") Long applicationId, @PathVariable("position") int position) {
        return applicationFormService.findApplicationForm(applicationId, position);
    }

    @PostMapping
    public ApplicationFormDto createApplicationForm(
            @RequestBody ApplicationFormDto applicationFormDto) {
        return applicationFormService.save(applicationFormDto.applicationId(), applicationFormDto);
    }

    @PutMapping(path = "/{formId}")
    public ApplicationFormDto updateApplicationForm(
            @PathVariable("formId") Long formId, @RequestBody ApplicationFormDto applicationFormDto) {
        return applicationFormService.update(formId, applicationFormDto);
    }

    @GetMapping(path = "/forms/{applicationId}")
    public List<ApplicationFormDto> retrieveApplicationForms(
            @PathVariable("applicationId") Long applicationId) {
        return applicationFormService.findByApplicationId(applicationId);
    }

    @DeleteMapping(path = "/{formId}")
    public void deleteApplicationForm(@PathVariable("formId") Long formId) {
        applicationFormService.deleteApplicationForm(formId);
    }
}
