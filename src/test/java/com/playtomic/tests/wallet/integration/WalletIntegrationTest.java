package com.playtomic.tests.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.services.stripe.StripeService;
import com.playtomic.tests.wallet.services.topup.TopUpService;
import com.playtomic.tests.wallet.services.wallet.WalletService;
import com.playtomic.tests.wallet.web.controller.WalletController;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.playtomic.tests.wallet.WalletTestUtil.TEST_TOP_UP_AMOUNT;
import static com.playtomic.tests.wallet.WalletTestUtil.createValidTopUpDto;
import static com.playtomic.tests.wallet.WalletTestUtil.createValidWalletDto;

@SpringBootTest
class WalletIntegrationTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletController walletController;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TopUpService topUpService;

    @Test
    void getWalletTest() {
        WalletDto walletDto = createValidWalletDto();

        walletDto = walletController.createWalletByUserId(walletDto).getBody();
        Assertions.assertNotNull(walletDto);

        TopUpDto topUpDto = createValidTopUpDto(walletDto);
        try {
            walletController.topUptoWallet(topUpDto);
            Assertions.assertEquals(TEST_TOP_UP_AMOUNT.intValue(), walletService.getWalletById(walletDto.getId()).getBalance().intValue());
        } finally {
            stripeService.refund(topUpDto.getPaymentId());
        }


    }

}
