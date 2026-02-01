package com.orakuma.rogator.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
    ApplicationEntity findByName(String name);
    List<ApplicationEntity> findByEmail(String email);

    @Query("select a from ApplicationEntity a where a.email = :email and a.status = :status")
    List<ApplicationEntity> findByEmailAndStatus(@Param("email") String email, @Param("status") String status);
}
