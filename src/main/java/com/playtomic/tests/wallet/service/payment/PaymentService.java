package com.playtomic.tests.wallet.service.payment;

import java.math.BigDecimal;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface PaymentService {
    void charge(String creditCardNumber, BigDecimal amount);
    void refund(String paymentId);
}
