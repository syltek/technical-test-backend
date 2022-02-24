package com.playtomic.tests.wallet.service.payment.stripe;

/**
 * Updated by Orkun Cavdar on 08/02/2022
 */
public class StripeAmountTooSmallException extends StripeServiceException {
    public StripeAmountTooSmallException(String message) {
        super(message);
    }
}
