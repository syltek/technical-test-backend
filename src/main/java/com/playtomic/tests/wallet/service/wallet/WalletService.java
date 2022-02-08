package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.PaymentDto;
import com.playtomic.tests.wallet.dto.WalletDto;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface WalletService {
    WalletDto getWallet(Long id);
    PaymentDto getPayment(Long id);
    boolean chargeMoneyWalletByCreditCard(Long id, ChargeRequestDto body);
    boolean chargeMoneyBackWalletByCreditCard(Long paymentId);
}
