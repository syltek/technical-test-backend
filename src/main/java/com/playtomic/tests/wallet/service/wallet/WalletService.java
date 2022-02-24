package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.PaymentDto;
import com.playtomic.tests.wallet.dto.StripePaymentDto;
import com.playtomic.tests.wallet.dto.WalletDto;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface WalletService {
    WalletDto getWallet(Long id);
    PaymentDto getPayment(String id);
    StripePaymentDto chargeMoneyWalletByCreditCard(Long id, ChargeRequestDto body);
    WalletDto chargeMoneyBackWalletByCreditCard(String paymentId);
}
