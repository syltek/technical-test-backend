package com.playtomic.exercise.payment.provider.stripe.config;

import java.net.URI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
  private Simulator simulator;
  private Request request;

  @Data
  public static class Simulator {
    private URI chargesUri;
    private URI refundsUri;
  }

  @Data
  public static class Request {
    private Timeout timeout;
    private Charge charge;
    private Refund refund;

    @Data
    public static class Timeout {
      private int connectionTimeout;
      private int readTimeout;
    }

    @Data
    public static class Charge {
      private int maxAttempts;
      private int delay;
      private int maxDelay;
    }

    @Data
    public static class Refund {
      private int maxAttempts;
      private int delay;
      private int maxDelay;
    }
  }
}
