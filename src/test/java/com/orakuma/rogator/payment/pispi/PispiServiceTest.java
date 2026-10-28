package com.orakuma.rogator.payment.pispi;

import com.orakuma.rogator.config.PaymentClientConfig;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;

public class PispiServiceTest {

  private PaymentClientConfig paymentClientConfig = mock(PaymentClientConfig.class);
  @InjectMocks private PispiService pispiService;

  @Test
  public void postPispiPayloadRequestReturnResponse() {
    String payerAlias = "sduhs78-23dws-wssz121-123ff";
    CreatePaymentRequestPayload paymentRequestPayload =
        new CreatePaymentRequestPayload(
            payerAlias,
            BigDecimal.valueOf(2),
            "orderXX",
            "test",
            false,
            "000",
            "2387d-23sd-234445f",
            "http://logo",
            LocalDateTime.now());
  }
}
