package com.playtomic.tests.wallet.services.wallet;

import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;

import java.util.UUID;

public interface WalletService {

    WalletDto getWalletById(UUID uuid);

    WalletDto createWalletByUserId(UUID userId);

    WalletDto topUpAmountToWallet(TopUpDto topUpDto);

}
