package com.playtomic.tests.wallet.service.payment;

import com.playtomic.tests.wallet.dto.StripePaymentDto;

import java.math.BigDecimal;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface PaymentService {
    StripePaymentDto charge(String creditCardNumber, BigDecimal amount);
    void refund(String paymentId);
}
