package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.service.payment.gateway.stripe.Payment;

import java.math.BigDecimal;

public interface PaymentService {

    Payment charge( String creditCardNumber, BigDecimal amount);

    void refund(String paymentId);
}
