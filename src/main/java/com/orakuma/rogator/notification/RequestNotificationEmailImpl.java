package com.orakuma.rogator.notification;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

  private final JavaMailSender mailSender;
  private final MessageSource messageSource;

  @Async
  @Override
  public void notifyRequestor(
      String serviceTitle, String trackingCode, BigDecimal price, String toEmail) {
    Locale locale = LocaleContextHolder.getLocale();
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    String formattedPrice = currencyFormatter.format(price);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(
        messageSource.getMessage(
            "email.request.confirmation.subject", new Object[] {serviceTitle}, locale));
    message.setText(
        messageSource.getMessage(
            "email.request.confirmation.body",
            new Object[] {serviceTitle, formattedPrice, trackingCode},
            locale));

    try {
      this.mailSender.send(message);
    } catch (MailException ex) {
      log.error("Error sending email", ex);
      System.err.println(ex.getMessage());
    }
  }
}
