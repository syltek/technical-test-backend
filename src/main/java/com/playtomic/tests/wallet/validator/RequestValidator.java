package com.playtomic.tests.wallet.validator;

import com.playtomic.tests.wallet.dto.RequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface RequestValidator<T, U> {
    void validate(T dto, U dto1);
}
