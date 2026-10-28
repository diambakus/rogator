package com.orakuma.rogator.payment;

import com.orakuma.rogator.payment.pispi.CreatePaymentRequestResponse;
import com.orakuma.rogator.payment.pispi.PaymentRequestDto;
import com.orakuma.rogator.payment.pispi.PaymentStatusResponse;
import com.paypal.orders.Order;

public interface PaymentService {
  String createOrder(String applicationId, PaymentTypeRequest paymentTypeRequest);

  Order captureOrder(String orderId);

  CreatePaymentRequestResponse requestPispiPayment(
      PaymentRequestDto paymentRequestDto,
      PaymentTypeRequest paymentTypeRequest);
  PaymentStatusResponse getPispiPaymentStatus(String txId);
}