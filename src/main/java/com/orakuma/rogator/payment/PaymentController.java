package com.orakuma.rogator.payment;

import com.paypal.orders.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "payments")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/paypal/orders")
  public ResponseEntity<Map<String, String>> createOrder(@RequestBody CreateOrderRequest orderRequest) {
    String applicationId = orderRequest.applicationId();
    String orderId = paymentService.createOrder(applicationId, PaymentTypeRequest.PAYPAL);
    return ResponseEntity.ok(Map.of("orderId", orderId));
  }

  @PostMapping("/paypal/orders/{orderId}/capture")
  public Order captureOrder(@PathVariable String orderId) {
    return paymentService.captureOrder(orderId);
  }
}
