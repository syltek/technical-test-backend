package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getWallet(String id);

    Wallet topUp(String walletId, String creditCardNumber, BigDecimal amount);
}
