package com.orakuma.rogator.application_form;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping(path = "application-form")
public class ApplicationFormController {
    private final ApplicationFormService applicationFormService;

    public ApplicationFormController(ApplicationFormService applicationFormService) {
        this.applicationFormService = applicationFormService;
    }

    @GetMapping(path = "/{formId}")
    public ApplicationFormDto getApplicationForm(@PathVariable("formId") Long formId) {
        return applicationFormService.getApplicationForm(formId);
    }

    @PostMapping(path = "/{applicationId}")
    public ApplicationFormDto postApplicationForm(
            @PathVariable("applicationId") Long applicationId,
            @RequestBody ApplicationFormDto applicationFormDto
    ) {
        return applicationFormService.save(applicationId, applicationFormDto);
    }

    @PutMapping(path = "/{formId}")
    public ApplicationFormDto putApplicationForm(
            @PathVariable("formId") Long formId,
            @RequestBody ApplicationFormDto applicationFormDto
    ) {
        return applicationFormService.update(formId, applicationFormDto);
    }

    @GetMapping(path = "/forms/{applicationId}")
    public List<ApplicationFormDto> getApplicationForms(@PathVariable("applicationId") Long applicationId) {
        return applicationFormService.findByApplicationId(applicationId);
    }

    @DeleteMapping(path = "/{formId}")
    public void deleteApplicationForm(@PathVariable("formId") Long formId) {
        applicationFormService.deleteApplicationForm(formId);
    }
}
