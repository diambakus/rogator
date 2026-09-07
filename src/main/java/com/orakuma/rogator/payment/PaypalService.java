package com.orakuma.rogator.payment;

import com.orakuma.rogator.config.payment.paypal.PaypalProperties;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaypalService {
  private final PayPalHttpClient paypalHttpClient;
  private final PaypalProperties paypalProperties;

  public String createOrder(BigDecimal amount) throws IOException {
    OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest();
    ordersCreateRequest.prefer("return=representation");
    OrderRequest orderRequest =
        new OrderRequest()
            .checkoutPaymentIntent("CAPTURE")
            .purchaseUnits(
                List.of(
                    new PurchaseUnitRequest()
                        .amountWithBreakdown(
                            new AmountWithBreakdown()
                                .currencyCode(paypalProperties.currencyCode())
                                .value(amount.toString()))));
    ordersCreateRequest.requestBody(orderRequest);

    log.info("Calling Paypal client to make order request");
    HttpResponse<Order> orderResponse = paypalHttpClient.execute(ordersCreateRequest);
    return orderResponse.result().id();
  }

  public Order captureOrder(String orderId) throws IOException {
    OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(orderId);
    ordersCaptureRequest.requestBody(new OrderActionRequest());
    log.info("Calling Paypal client to capture order request");
    return paypalHttpClient.execute(ordersCaptureRequest).result();
  }
}
