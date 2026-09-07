package com.orakuma.rogator.payment;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.application.ApplicationStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentPersistenceService {

  private final PaymentRepository paymentRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void saveOrUpdatePayment(
      ApplicationEntity application, String orderId, PaymentType paymentType) {
    paymentRepository
        .findByApplicationId(application.getId())
        .ifPresentOrElse(
            payment -> {
              payment.setOrderId(orderId);
              payment.setPaymentType(paymentType);
            },
            () ->
                paymentRepository.save(
                    new PaymentEntity()
                        .setOrderId(orderId)
                        .setPaymentType(paymentType)
                        .setCreated(LocalDateTime.now())
                        .setApplication(application)));
  }

  @Transactional
  public void completePaymentAndApplication(Long paymentId) {
    PaymentEntity paymentEntity =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(
                () -> new EntityNotFoundException("Payment not found for ID: " + paymentId));

    paymentEntity.setUpdated(LocalDateTime.now());

    ApplicationEntity application = paymentEntity.getApplication();
    if (application != null) {
      application.setStatus(ApplicationStatus.PAID);

      eventPublisher.publishEvent(
          new OrderCapturedEvent(
              application.getName(),
              application.getTrackingCode(),
              application.getPrice(),
              application.getEmail()));
    }
  }
}
