package com.orakuma.rogator.payment;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.application.ApplicationRepository;
import com.orakuma.rogator.payment.exceptions.PaymentProcessingException;
import com.paypal.orders.Order;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaypalService paypalService;
  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;
  private final ApplicationRepository applicationRepository;
  private final PaymentPersistenceService paymentPersistenceService;

  @Override
  public String createOrder(String applicationId, PaymentTypeRequest paymentTypeRequest) {
    if (Objects.requireNonNullElse(applicationId, "").isBlank()) {
      throw new IllegalArgumentException("applicationId cannot be null or empty");
    }

    ApplicationEntity application =
            applicationRepository
                    .findByPublicId(applicationId)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Application not found for ID: " + applicationId));

    PaymentType paymentType = paymentMapper.mapToDomain(paymentTypeRequest);

    String orderId =
            switch (paymentTypeRequest) {
              case PAYPAL -> executePaypalOrderCreation(application);
              default -> throw new UnsupportedOperationException(
                      "Unsupported payment type: " + paymentTypeRequest);
            };

    paymentPersistenceService.saveOrUpdatePayment(application, orderId, paymentType);
    return orderId;
  }

  private String executePaypalOrderCreation(ApplicationEntity application) {
    log.info("Executing PayPal order creation for application: {}.", application.getPublicId());
    try {
      return paypalService.createOrder(application.getPrice());
    } catch (IOException e) {
      log.error("Failed to create PayPal order for application {}", application.getPublicId());
      throw new PaymentProcessingException("Failed to create PayPal order for application " + e);
    }
  }

  @Override
  public Order captureOrder(String orderId) {
    if (Objects.requireNonNullElse(orderId, "").isBlank()) {
      throw new IllegalArgumentException("orderId cannot be null or empty");
    }

    PaymentEntity paymentEntity =
            paymentRepository
                    .findByOrderId(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

    Order resultOrder = executePaypalCaptureOrder(orderId);

    paymentPersistenceService.completePaymentAndApplication(paymentEntity.getId());

    return resultOrder;
  }

  private Order executePaypalCaptureOrder(String orderId) {
    log.info("Capturing PayPal order for orderId {}", orderId);
    try {
      return paypalService.captureOrder(orderId);
    } catch (IOException e) {
      log.error("Error capturing order for orderId {}", orderId);
      throw new PaymentProcessingException("Error capturing order for orderId " + e);
    }
  }
}