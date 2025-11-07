package com.orakuma.rogator.application_form;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationFormRepository extends
        CrudRepository<ApplicationFormEntity, Long>, ApplicationFormRepositoryCustom {
    @Query("select af from ApplicationFormEntity af where af.application.id = :applicationId")
    List<ApplicationFormEntity> findAllByApplicationId(Long applicationId);
}
