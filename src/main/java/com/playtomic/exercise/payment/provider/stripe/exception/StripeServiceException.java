package com.playtomic.exercise.payment.provider.stripe.exception;

/**
 * Exception thrown when there is a general error in the Stripe service.
 */
public class StripeServiceException extends RuntimeException {

  /**
   * Constructs a new StripeServiceException with the specified detail message.
   *
   * @param message the detail message
   */
  public StripeServiceException(String message) {
    super(message);
  }
}
