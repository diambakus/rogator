package com.orakuma.rogator.config;

import com.orakuma.rogator.config.payment.pispi.PispiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@Slf4j
public class PaymentClientConfig {

  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
  private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

  @Bean
  public RestClient piSpiRestClient(
      RestClient.Builder builder,
      OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
      PispiProperties pispiProperties,
      RestClientSsl restClientSsl) {

    OAuth2ClientHttpRequestInterceptor oAuth2ClientHttpRequestInterceptor =
        new OAuth2ClientHttpRequestInterceptor(oAuth2AuthorizedClientManager);
    oAuth2ClientHttpRequestInterceptor.setClientRegistrationIdResolver(request -> "pi-spi");

    if (StringUtils.hasText(pispiProperties.sslBundle())) {
      // mTLS: client certificate comes from spring.ssl.bundle.jks.<name>
      log.info("PI-SPI RestClient using mTLS with SSL bundle '{}'", pispiProperties.sslBundle());
      builder.apply(restClientSsl.fromBundle(pispiProperties.sslBundle()));
    } else {
      log.warn(
          "PI-SPI RestClient has NO client certificate (payment.pispi.ssl-bundle is empty). "
              + "The PI-SPI gateway requires mTLS and will likely reset the connection.");
      JdkClientHttpRequestFactory factory =
          new JdkClientHttpRequestFactory(
              HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT).build());
      factory.setReadTimeout(READ_TIMEOUT);
      builder.requestFactory(factory);
    }

    return builder
        .baseUrl(pispiProperties.baseUrl())
        .requestInterceptor(oAuth2ClientHttpRequestInterceptor)
        .defaultHeader("x-api-key", pispiProperties.apiKey())
        .build();
  }
}
