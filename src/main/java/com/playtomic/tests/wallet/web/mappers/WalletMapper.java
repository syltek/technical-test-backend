package com.playtomic.tests.wallet.web.mappers;


import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.web.model.WalletDto;
import org.mapstruct.Mapper;

@Mapper
public interface WalletMapper {

    WalletDto walletToWalletDto(Wallet wallet);

    Wallet walletDtoToWallet(WalletDto walletDto);
}
