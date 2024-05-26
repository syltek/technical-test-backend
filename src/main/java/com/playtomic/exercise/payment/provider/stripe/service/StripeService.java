package com.playtomic.exercise.payment.provider.stripe.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.exercise.payment.model.Payment;
import com.playtomic.exercise.payment.provider.stripe.config.StripeProperties;
import com.playtomic.exercise.payment.provider.stripe.exception.StripeServiceException;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to Stripe using their API/SDK. This dummy implementation throws
 * an error when trying to charge less than 10€.
 */
@Service
@RequiredArgsConstructor
public class StripeService implements IStripeService {
  @NonNull
  private final StripeProperties stripeProperties;

  @NonNull
  private final RestTemplate restTemplate;

  /**
   * Charges money to the credit card.
   * <p>
   * Ignore the fact that no CVC or expiration date are provided.
   * <p>
   * This method retries the operation in case of specific server-side errors or too many requests
   * errors, using exponential backoff strategy with randomization to avoid overwhelming the
   * server.
   *
   * @param creditCardNumber the number of the credit card
   * @param amount           the amount that will be charged
   * @return the {@link Payment} details
   * @throws StripeServiceException if there is an error during the charge process
   */
  @Retryable(
    label = "stripe-charge-retry-charge",
    include = {
      HttpServerErrorException.class,
      HttpClientErrorException.TooManyRequests.class
    },
    maxAttemptsExpression = "#{${stripe.request.charge.max-attempts:10}}",
    backoff = @Backoff(
      delayExpression = "#{${stripe.request.charge.delay:1000}}",
      maxDelayExpression = "#{${stripe.request.charge.max-delay:10000}}",
      multiplier = 2,
      random = true
    )
  )
  @Override
  public Payment charge(
    @NonNull String creditCardNumber,
    @NonNull BigDecimal amount
  )
    throws StripeServiceException {
    final ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
    return restTemplate.postForObject(
      stripeProperties.getSimulator().getChargesUri(),
      body,
      Payment.class
    );
  }

  /**
   * Refunds the specified payment.
   * <p>
   * This method retries the operation in case of specific server-side errors or too many requests
   * errors, using exponential backoff strategy with randomization to avoid overwhelming the
   * server.
   *
   * @param paymentId the ID of the payment to refund
   * @throws StripeServiceException if there is an error during the refund process
   */
  @Retryable(
    label = "stripe-refund-retry-refund",
    include = {
      HttpServerErrorException.class,
      HttpClientErrorException.TooManyRequests.class
    },
    maxAttemptsExpression = "#{${stripe.request.refund.max-attempts:10}}",
    backoff = @Backoff(
      delayExpression = "#{${stripe.request.refund.delay:1000}}",
      maxDelayExpression = "#{${stripe.request.refund.max-delay:10000}}",
      multiplier = 2,
      random = true
    )
  )
  @Override
  public void refund(@NonNull String paymentId) throws StripeServiceException {
    // Object.class because we don't read the body here.
    restTemplate.postForEntity(
      stripeProperties.getSimulator().getChargesUri().toString(),
      null,
      Object.class,
      paymentId
    );
  }

  /**
   * Represents a charge request to Stripe.
   */
  @AllArgsConstructor
  public static class ChargeRequest {
    /**
     * The credit card number.
     */
    @NonNull
    @JsonProperty("credit_card")
    String creditCardNumber;

    /**
     * The amount to be charged.
     */
    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;
  }
}
