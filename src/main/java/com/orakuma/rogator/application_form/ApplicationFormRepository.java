package com.orakuma.rogator.application_form;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationFormRepository
    extends CrudRepository<ApplicationFormEntity, Long>, ApplicationFormRepositoryCustom {
  @Query("select af from ApplicationFormEntity af where af.application.id = :applicationId")
  List<ApplicationFormEntity> findAllByApplicationId(@Param("applicationId") Long applicationId);

  @Query(
      """
          SELECT afe
          FROM ApplicationFormEntity afe
          WHERE afe.application.publicId = :applicationPublicId
        """)
  List<ApplicationFormEntity> findApplicationFormEntitiesByApplicationPublicId(
      @Param("applicationPublicId") String applicationPublicId);

  @Query(
"""
            SELECt afe
            FROM ApplicationFormEntity  afe
            WHERE afe.application.id = :applicationId
            AND afe.position = :position
        """)
  ApplicationFormEntity findByApplicationIdAndPosition(
      @Param("applicationId") Long applicationId, @Param("position") int position);
}
