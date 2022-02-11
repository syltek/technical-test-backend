package com.playtomic.tests.wallet.web.mappers;

import com.playtomic.tests.wallet.domain.TopUp;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import org.mapstruct.Mapper;

@Mapper
public interface TopUpMapper {

    TopUpDto topUpToTopUpDto(TopUp topUp);

    TopUp topUpDtoToTopUp(TopUpDto topUpDto);

}
