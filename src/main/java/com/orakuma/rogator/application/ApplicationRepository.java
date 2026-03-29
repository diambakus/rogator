package com.orakuma.rogator.application;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
  ApplicationEntity findByName(String name);

  List<ApplicationEntity> findByEmail(String email);

  @Query("select a from ApplicationEntity a where a.email = :email and a.status = :status")
  List<ApplicationEntity> findByEmailAndStatus(
      @Param("email") String email, @Param("status") String status);

  @Query(
"""
    select a from ApplicationEntity a
    where
        (a.assigneeId = :employeeId and a.status = :processing)
        or
        (a.assigneeId is null and a.status = :created)
""")
  List<ApplicationEntity> findRelevantApplications(
      @Param("employeeId") String employeeId,
      @Param("processing") ApplicationStatus processing,
      @Param("created") ApplicationStatus created);

  ApplicationEntity findByPublicId(String publicId);

  void deleteByPublicId(String publicId);

  @Modifying
  @Query(
"""
 update ApplicationEntity app
 set app.status = :abandoned
 where app.status in :statuses
 and app.expiresAt <= CURRENT_TIMESTAMP
""")
  int markAsAbandoned(
      @Param("statuses") List<ApplicationStatus> statuses,
      @Param("abandoned") ApplicationStatus abandoned);

  @Query(
"""
                select a from ApplicationEntity  a
                where a.requestorId = :requestorId
                and a.status in :statuses
        """)
  List<ApplicationEntity> findByRequestorIdAndStatus(
          @Param("requestorId") String requestorId,
          @Param("statuses") List<ApplicationStatus> statuses);
}