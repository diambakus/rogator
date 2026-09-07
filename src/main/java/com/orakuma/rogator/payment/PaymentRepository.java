package com.orakuma.rogator.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByApplicationId(Long applicationId);
    Optional<PaymentEntity> findByOrderId(String orderId);
}
