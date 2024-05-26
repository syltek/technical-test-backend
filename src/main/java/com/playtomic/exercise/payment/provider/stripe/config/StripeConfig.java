package com.playtomic.exercise.payment.provider.stripe.config;

import com.playtomic.exercise.payment.provider.stripe.service.StripeRestTemplateResponseErrorHandler;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StripeConfig {

  @Bean
  public RestTemplate stripeRestTemplate(
    RestTemplateBuilder restTemplateBuilder,
    StripeProperties stripeProperties
  ) {
    return restTemplateBuilder
      .errorHandler(new StripeRestTemplateResponseErrorHandler())
      .setConnectTimeout(
        Duration.ofMillis(
          stripeProperties.getRequest().getTimeout().getConnectionTimeout()
        )
      )
      .setReadTimeout(
        Duration.ofMillis(
          stripeProperties.getRequest().getTimeout().getReadTimeout()
        )
      )
      .build();
  }
}
