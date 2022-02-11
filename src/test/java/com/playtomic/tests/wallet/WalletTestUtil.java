package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletTestUtil {

    public final static String TEST_CREDIT_CARD_NUMBER = "4242 4242 4242 4242";
    public final static BigDecimal TEST_TOP_UP_AMOUNT = BigDecimal.valueOf(20.0);


    public static WalletDto createValidWalletDto() {
        return WalletDto.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();
    }

    public static TopUpDto createValidTopUpDto(WalletDto walletDto) {
        return TopUpDto.builder()
                .id(UUID.randomUUID())
                .walletId(walletDto.getId())
                .amount(TEST_TOP_UP_AMOUNT)
                .creditCardNumber(TEST_CREDIT_CARD_NUMBER)
                .build();
    }

}
