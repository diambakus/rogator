package com.orakuma.rogator.notification;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@Slf4j
@Setter
@RequiredArgsConstructor
public class RequestNotificationEmailImpl implements RequestNotification {

  private static final String REQUEST_SERVICE_CONFIRMATION_TEMPLATE =
      """
        Olá!

        Confirmamos o recebimento do seu pagamento para o serviço:
        - **Serviço:** %s
        - **Valor Pago:** %s
        - **Código de acompanhamento:** %s

        Obrigado pela sua compra! Em caso de dúvidas, entre em contato com o suporte.
    """;
  private final JavaMailSender mailSender;

  @Async
  @Override
  public void notifyRequestor(
      String serviceTitle, String trackingCode, BigDecimal price, String toEmail) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Sua compra - " + serviceTitle);

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("pt", "PT"));
    String formattedPrice = currencyFormatter.format(price);
    message.setText(
        REQUEST_SERVICE_CONFIRMATION_TEMPLATE.formatted(
            serviceTitle, formattedPrice, trackingCode));
    try {
      this.mailSender.send(message);
    } catch (MailException ex) {
      log.error("Error sending email", ex);
      System.err.println(ex.getMessage());
    }
  }
}
