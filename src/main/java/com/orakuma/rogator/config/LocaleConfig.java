package com.orakuma.rogator.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);
    localeResolver.setSupportedLocales(
        List.of(
            Locale.US,
            Locale.of("pt", "BR"),
            Locale.of("pt", "PT"),
            Locale.of("pt"),
            Locale.FRANCE,
            Locale.of("fr"),
            Locale.of("es", "ES"),
            Locale.of("es")));
    return localeResolver;
  }
}
