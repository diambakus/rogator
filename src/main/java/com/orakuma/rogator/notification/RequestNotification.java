package com.orakuma.rogator.notification;

import java.math.BigDecimal;

public interface RequestNotification {
  void notifyRequestor(String serviceTitle, String trackingCode, BigDecimal price, String toEmail);
}
