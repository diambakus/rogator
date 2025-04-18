package com.orakuma.rogator.application_form;

import java.util.List;

public interface ApplicationFormService {
    ApplicationFormDto update(Long formId, ApplicationFormDto applicationFormDto);
    List<ApplicationFormDto> save(List<ApplicationFormDto> applicationsForms);
    boolean delete(ApplicationFormDto applicationFormDto);
    List<ApplicationFormDto> findByApplicationId(Long id);
    ApplicationFormDto save(Long applicationId, ApplicationFormDto applicationFormDto);
    ApplicationFormDto getApplicationForm(Long formId);
}
