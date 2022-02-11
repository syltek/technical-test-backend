package com.playtomic.tests.wallet.services.topup;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.web.model.TopUpDto;

public interface TopUpService {
    void saveTopUp(TopUpDto topUpDto, Wallet wallet);

    void charge(TopUpDto topUpDto);
}
