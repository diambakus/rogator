package com.orakuma.rogator.application;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
  List<ApplicationEntity> findByEmail(String email);

  @Query("select a from ApplicationEntity a where a.email = :email and a.status = :status")
  List<ApplicationEntity> findByEmailAndStatus(
      @Param("email") String email, @Param("status") String status);

  @Query(
"""
    select a from ApplicationEntity a
    where
        (a.requestedToUnitId = :unitId)
        and
        ((a.assigneeId = :employeeId and a.status = :processing)
        or
        (a.assigneeId is null and a.status = :created))
""")
  List<ApplicationEntity> findRelevantApplications(
      @Param("employeeId") String employeeId,
      @Param("unitId") Long unitId,
      @Param("processing") ApplicationStatus processing,
      @Param("created") ApplicationStatus created);

  @Query("select a from ApplicationEntity a where a.publicId = :publicId")
  Optional<ApplicationEntity> findByPublicId(@Param("publicId") String publicId);

  void deleteByPublicId(String publicId);

  @Query(
      """
                select a from ApplicationEntity  a
                where a.requestorId = :requestorId
                and a.status in :statuses
        """)
  List<ApplicationEntity> findByRequestorIdAndStatus(
      @Param("requestorId") String requestorId,
      @Param("statuses") List<ApplicationStatus> statuses);

  @Modifying
  @Query("""
           update ApplicationEntity app
           set app.status = :newStatus
           where app.status = :currentStatus
           and app.created < :cutoffTime
          """)
  int updateStatusForExpiredApplications(
      @Param("currentStatus") ApplicationStatus currentStatus,
      @Param("newStatus") ApplicationStatus newStatus,
      @Param("cutoffTime") LocalDateTime cutoffTime);
}