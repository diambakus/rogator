package com.orakuma.rogator.config;

import com.orakuma.rogator.config.payment.pispi.PispiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentClientConfig {

  @Bean
  public RestClient piSpiRestClient(
      RestClient.Builder builder,
      OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
      PispiProperties pispiProperties) {

    OAuth2ClientHttpRequestInterceptor oAuth2ClientHttpRequestInterceptor =
        new OAuth2ClientHttpRequestInterceptor(oAuth2AuthorizedClientManager);
    oAuth2ClientHttpRequestInterceptor.setClientRegistrationIdResolver(request -> "pi-spi");

    return builder
        .baseUrl(pispiProperties.baseUrl())
        .requestInterceptor(oAuth2ClientHttpRequestInterceptor)
        .defaultHeader("x-api-key", pispiProperties.apiKey())
        .defaultHeader("X-API-KEY", pispiProperties.apiKey())
        .build();
  }
}
