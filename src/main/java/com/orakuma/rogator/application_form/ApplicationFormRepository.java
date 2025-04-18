package com.orakuma.rogator.application_form;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationFormRepository extends
        CrudRepository<ApplicationFormEntity, Long>, ApplicationFormRepositoryCustom {
    @Query(value = "select * from application_forms where applicationId= :applicationId", nativeQuery = true)
    List<ApplicationFormEntity> findAllByApplicationId(Long applicationId);
}
