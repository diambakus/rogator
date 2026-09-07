package com.orakuma.rogator.config.payment.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaypalConfiguration {

  @Bean
  public PayPalHttpClient payPalHttpClient(PaypalProperties properties) {

    PayPalEnvironment environment;
      if (properties.mode().equalsIgnoreCase("live")) {
          environment =
                  new PayPalEnvironment.Live(properties.clientId(), properties.clientSecret());
          log.info("Using paypal live payment client");
      } else {
          environment =
                  new PayPalEnvironment.Sandbox(properties.clientId(), properties.clientSecret());
        log.info("Using paypal sandbox payment client");
      }

    return new PayPalHttpClient(environment);
  }
}