package com.orakuma.rogator.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class RequestNotificationEmailImplTest {

  @Mock private JavaMailSender mailSender;

  @AfterEach
  void tearDown() {
    LocaleContextHolder.resetLocaleContext();
  }

  @Test
  void shouldUseEnglishMessagesAndLocaleAwareFormatting() {
    SimpleMailMessage sentMessage = sendEmail(Locale.US, "Course", "ABC123");

    assertEquals("Your purchase - Course", sentMessage.getSubject());
    assertTrue(sentMessage.getText().contains("Hello!"));
    assertTrue(sentMessage.getText().contains("Service: Course"));
    assertTrue(sentMessage.getText().contains("Amount paid: $20.00"));
    assertTrue(sentMessage.getText().contains("Tracking code: ABC123"));
  }

  @Test
  void shouldUsePortugueseMessagesAndLocaleAwareFormatting() {
    SimpleMailMessage sentMessage = sendEmail(Locale.of("pt", "BR"), "Curso", "ABC123");

    assertEquals("Sua compra - Curso", sentMessage.getSubject());
    assertTrue(sentMessage.getText().contains("Olá!"));
    assertTrue(sentMessage.getText().contains("Serviço: Curso"));
    assertTrue(sentMessage.getText().contains("Valor pago:"));
    assertTrue(sentMessage.getText().contains("R$"));
    assertTrue(sentMessage.getText().contains("20,00"));
    assertTrue(sentMessage.getText().contains("Código de acompanhamento: ABC123"));
  }

  @Test
  void shouldUseFrenchMessagesAndLocaleAwareFormatting() {
    SimpleMailMessage sentMessage = sendEmail(Locale.FRANCE, "Cours", "ABC123");

    assertEquals("Achat - Cours", sentMessage.getSubject());
    assertTrue(sentMessage.getText().contains("Bonjour !"));
    assertTrue(sentMessage.getText().contains("Service : Cours"));
    assertTrue(sentMessage.getText().contains("Montant payé :"));
    assertTrue(sentMessage.getText().contains("20,00"));
    assertTrue(sentMessage.getText().contains("Code de suivi : ABC123"));
  }

  @Test
  void shouldUseSpanishMessagesAndLocaleAwareFormatting() {
    SimpleMailMessage sentMessage = sendEmail(Locale.of("es", "ES"), "Curso", "ABC123");

    assertEquals("Tu compra - Curso", sentMessage.getSubject());
    assertTrue(sentMessage.getText().contains("¡Hola!"));
    assertTrue(sentMessage.getText().contains("Servicio: Curso"));
    assertTrue(sentMessage.getText().contains("Monto pagado:"));
    assertTrue(sentMessage.getText().contains("20,00"));
    assertTrue(sentMessage.getText().contains("Código de seguimiento: ABC123"));
  }

  private SimpleMailMessage sendEmail(Locale locale, String serviceTitle, String trackingCode) {
    MessageSource messageSource = new ReloadableResourceBundleMessageSource();
    ((ReloadableResourceBundleMessageSource) messageSource).setBasename("messages");
    ((ReloadableResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");

    LocaleContextHolder.setLocale(locale);
    RequestNotificationEmailImpl service = new RequestNotificationEmailImpl(mailSender, messageSource);

    service.notifyRequestor(serviceTitle, trackingCode, new BigDecimal("20.00"), "user@example.com");

    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(mailSender).send(captor.capture());
    return captor.getValue();
  }
}
