package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.WalletTestUtil;
import com.playtomic.tests.wallet.services.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.services.stripe.StripeService;
import com.playtomic.tests.wallet.services.stripe.StripeServiceException;
import com.playtomic.tests.wallet.web.model.StripeChargeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */
@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    final ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
    @Mock
    StripeService stripeService;

    @BeforeEach
    public void init() {

        Mockito.doAnswer(invocation -> {
            BigDecimal amount = (BigDecimal) invocation.getArguments()[1];
            if (amount.compareTo(BigDecimal.valueOf(10)) < 0) {
                throw new StripeAmountTooSmallException();
            }
            return StripeChargeResponse.builder().paymentId(UUID.randomUUID().toString()).amount(amount).build();
        }).when(stripeService).charge(Mockito.anyString(), amountCaptor.capture());
    }

    @Test
    void test_exception() {
        BigDecimal amount = new BigDecimal(5);
        Assertions.assertThrows(StripeAmountTooSmallException.class, () ->
                stripeService.charge(WalletTestUtil.TEST_CREDIT_CARD_NUMBER, amount));
    }

    @Test
    void test_ok() throws StripeServiceException {
        StripeChargeResponse charge = stripeService.charge(WalletTestUtil.TEST_CREDIT_CARD_NUMBER, new BigDecimal(15));
        Assertions.assertEquals(BigDecimal.valueOf(15), charge.getAmount());
    }
}
