package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.WalletApplication;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
@SpringBootTest(classes = WalletApplication.class)
public class StripeServiceTest {

    //this is not using test profile, so this will test the simulator endpoint.

    @Autowired
    @InjectMocks
    StripeService s;

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
