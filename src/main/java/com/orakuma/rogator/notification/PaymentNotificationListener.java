package com.orakuma.rogator.notification;

import com.orakuma.rogator.payment.OrderCapturedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentNotificationListener {
  private final RequestNotification requestNotification;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCaptured(OrderCapturedEvent event) {
    requestNotification.notifyRequestor(
        event.requestingServiceTitle(),
        event.trackingCode(),
        event.price(),
        event.requestorEmail());
  }
}
