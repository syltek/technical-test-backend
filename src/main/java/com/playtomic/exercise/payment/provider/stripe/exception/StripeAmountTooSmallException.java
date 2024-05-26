package com.playtomic.exercise.payment.provider.stripe.exception;

/**
 * Exception thrown when the amount for a Stripe payment is too small.
 */
public class StripeAmountTooSmallException extends StripeServiceException {

  /**
   * Constructs a new StripeAmountTooSmallException with the specified detail message.
   *
   * @param message the detail message
   */
  public StripeAmountTooSmallException(String message) {
    super(message);
  }
}
