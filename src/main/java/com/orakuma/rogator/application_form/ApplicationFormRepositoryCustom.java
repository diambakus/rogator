package com.orakuma.rogator.application_form;

public interface ApplicationFormRepositoryCustom {
    ApplicationFormEntity saveWithJsonb(ApplicationFormEntity entity, Long applicationId);
    ApplicationFormEntity updateWithJsonb(ApplicationFormEntity entity, Long applicationId);
}
