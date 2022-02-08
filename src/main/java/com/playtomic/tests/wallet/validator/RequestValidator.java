package com.playtomic.tests.wallet.validator;

import com.playtomic.tests.wallet.dto.RequestDto;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface RequestValidator {
    void validate(RequestDto requestDto);
}
