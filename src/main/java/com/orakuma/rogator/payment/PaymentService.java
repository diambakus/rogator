package com.orakuma.rogator.payment;

import com.paypal.orders.Order;

public interface PaymentService {
    String createOrder(String applicationId, PaymentTypeRequest paymentTypeRequest);
    Order captureOrder(String orderId);
}
