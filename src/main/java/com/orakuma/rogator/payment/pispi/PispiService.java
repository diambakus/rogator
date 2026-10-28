package com.orakuma.rogator.payment.pispi;

import com.orakuma.rogator.config.payment.pispi.PispiProperties;
import com.orakuma.rogator.payment.exceptions.PaymentProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class PispiService {
  private final RestClient restClient;
  private final PispiPaymentMapper pispiPaymentMapper;
  private final PispiProperties pispiProperties;

  public CreatePaymentRequestResponse requestPayment(
      PispiAndApplicationInput pispiAndApplicationInput) {
    CreatePaymentRequestPayload requestPayload =
        pispiPaymentMapper.map(
            pispiAndApplicationInput,
            pispiProperties,
            false,
            LocalDateTime.now().plusMinutes(pispiProperties.durationLimitPayment()));

    return restClient
        .post()
        .uri(pispiProperties.paymentUrl())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestPayload)
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            (request, response) -> {
              log.error("Could not request failed: {}", request);
              throw new PaymentProcessingException(response.getStatusText());
            })
        .onStatus(
            HttpStatusCode::is5xxServerError,
            (request, response) -> {
              log.error("Server could not process request: {}", request);
              throw new PaymentProcessingException(response.getStatusText());
            })
        .body(CreatePaymentRequestResponse.class);
  }

  public PaymentStatusResponse getPaymentStatus(String txId) {
    return restClient
        .get()
        .uri(pispiProperties.paymentStatus() + "/{txId}", txId)
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            (request, response) -> {
              log.error("Could not request failed: {}", request);
              throw new PaymentProcessingException(response.getStatusText());
            })
        .onStatus(
            HttpStatusCode::is5xxServerError,
            (request, response) -> {
              log.error("Server could not process request: {}", request);
              throw new PaymentProcessingException(response.getStatusText());
            })
        .body(PaymentStatusResponse.class);
  }
}
