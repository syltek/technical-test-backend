package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.WalletTestUtil;
import com.playtomic.tests.wallet.domain.TopUp;
import com.playtomic.tests.wallet.repositories.TopUpRepository;
import com.playtomic.tests.wallet.services.stripe.StripeService;
import com.playtomic.tests.wallet.services.topup.TopUpService;
import com.playtomic.tests.wallet.services.wallet.WalletService;
import com.playtomic.tests.wallet.web.mappers.WalletMapper;
import com.playtomic.tests.wallet.web.model.StripeChargeResponse;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    WalletService walletService;

    @Autowired
    TopUpService topUpService;

    @MockBean
    StripeService stripeService;

    @Autowired
    TopUpRepository topUpRepository;

    @Autowired
    WalletMapper walletMapper;

    WalletDto validWalletDto;

    @BeforeEach
    void init() {
        UUID userId = UUID.randomUUID();
        validWalletDto = walletService.createWalletByUserId(userId);
    }

    @Test
    void getWalletTest() {
        WalletDto notSavedWallet = WalletTestUtil.createValidWalletDto();
        TopUpDto validTopUpDto = WalletTestUtil.createValidTopUpDto(notSavedWallet);
        UUID notSavedWalletId = notSavedWallet.getId();
        Assertions.assertThrows(NoSuchElementException.class, () -> walletService.getWalletById(notSavedWalletId));
        Assertions.assertThrows(NoSuchElementException.class, () -> walletService.topUpAmountToWallet(validTopUpDto));
        Assertions.assertNotEquals(walletService.getWalletById(validWalletDto.getId()).getId(), notSavedWalletId);
        Assertions.assertEquals(walletService.getWalletById(validWalletDto.getId()).getUserId(), validWalletDto.getUserId());
    }

    @Test
    void addTopUpToWalletTest() {

        TopUpDto validTopUp = WalletTestUtil.createValidTopUpDto(validWalletDto);
        Mockito.doAnswer(invocation -> new StripeChargeResponse("TestId", validTopUp.getAmount())).when(stripeService).charge(Mockito.anyString(), Mockito.eq(validTopUp.getAmount()));

        walletService.topUpAmountToWallet(validTopUp);
        WalletDto walletById = walletService.getWalletById(validWalletDto.getId());
        List<TopUp> topUpByWallet = topUpRepository.findAllByWallet(walletMapper.walletDtoToWallet(walletById));

        Assertions.assertEquals(1, topUpByWallet.size());
        Assertions.assertEquals(BigDecimal.valueOf(20).intValue(), walletById.getBalance().intValue());


//         Add the second pop up
        TopUpDto validTopUpDto2 = WalletTestUtil.createValidTopUpDto(validWalletDto);
        validTopUpDto2.setAmount(BigDecimal.valueOf(15.50));
        Mockito.doAnswer(invocation -> new StripeChargeResponse("TestId2", validTopUpDto2.getAmount())).when(stripeService).charge(Mockito.anyString(), Mockito.eq(validTopUpDto2.getAmount()));

        walletService.topUpAmountToWallet(validTopUpDto2);
        walletById = walletService.getWalletById(validWalletDto.getId());
        topUpByWallet = topUpRepository.findAllByWallet(walletMapper.walletDtoToWallet(walletById));

        Assertions.assertEquals(BigDecimal.valueOf(35.50), walletById.getBalance().stripTrailingZeros());
        Assertions.assertEquals(2, topUpByWallet.size());

    }


}
