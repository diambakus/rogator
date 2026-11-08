package com.orakuma.rogator.payment.pispi;

import com.orakuma.rogator.config.payment.pispi.PispiProperties;
import com.orakuma.rogator.payment.exceptions.PaymentProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
              String body = readBody(response);
              log.error("PI-SPI rejected request {} -> {}: {}", request.getURI(), response.getStatusCode(), body);
              throw new PaymentProcessingException(response.getStatusText() + ": " + body);
            })
        .onStatus(
            HttpStatusCode::is5xxServerError,
            (request, response) -> {
              String body = readBody(response);
              log.error("PI-SPI server error on {} -> {}: {}", request.getURI(), response.getStatusCode(), body);
              throw new PaymentProcessingException(response.getStatusText() + ": " + body);
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
              String body = readBody(response);
              log.error("PI-SPI rejected request {} -> {}: {}", request.getURI(), response.getStatusCode(), body);
              throw new PaymentProcessingException(response.getStatusText() + ": " + body);
            })
        .onStatus(
            HttpStatusCode::is5xxServerError,
            (request, response) -> {
              String body = readBody(response);
              log.error("PI-SPI server error on {} -> {}: {}", request.getURI(), response.getStatusCode(), body);
              throw new PaymentProcessingException(response.getStatusText() + ": " + body);
            })
        .body(PaymentStatusResponse.class);
  }

  private static String readBody(ClientHttpResponse response) {
    try {
      return new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      return "<unreadable body>";
    }
  }
}
