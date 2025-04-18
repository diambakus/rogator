package com.orakuma.rogator.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
    ApplicationEntity findByName(String name);
    List<ApplicationEntity> findByEmail(String email);
    @Query(value = "select * from applications a where a.email = :email and a.status = :status", nativeQuery = true)
    List<ApplicationEntity> findByEmailAndStatus(String email, String status);
}
