package com.playtomic.exercise.payment.provider.stripe.service;

import com.playtomic.exercise.payment.model.Payment;
import com.playtomic.exercise.payment.provider.stripe.exception.StripeServiceException;
import java.math.BigDecimal;

/**
 * Interface for managing Stripe operations.
 */
public interface IStripeService {
  /**
   * Charges money to the credit card.
   *
   * @param creditCardNumber the number of the credit card
   * @param amount the amount that will be charged
   * @return the {@link Payment} details
   * @throws StripeServiceException if there is an error during the charge process
   */
  Payment charge(String creditCardNumber, BigDecimal amount)
    throws StripeServiceException;

  /**
   * Refunds the specified payment.
   *
   * @param paymentId the ID of the payment to refund
   * @throws StripeServiceException if there is an error during the refund process
   */
  void refund(String paymentId) throws StripeServiceException;
}
