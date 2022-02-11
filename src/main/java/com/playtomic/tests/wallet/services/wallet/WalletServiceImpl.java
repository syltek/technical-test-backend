package com.playtomic.tests.wallet.services.wallet;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.repositories.WalletRepository;
import com.playtomic.tests.wallet.services.topup.TopUpService;
import com.playtomic.tests.wallet.web.mappers.WalletMapper;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    private final TopUpService topUpService;


    public WalletDto createWalletByUserId(UUID userId) {
        WalletDto walletDto = WalletDto.builder().userId(userId).balance(BigDecimal.ZERO).build();
        return walletMapper.walletToWalletDto(walletRepository.saveAndFlush(walletMapper.walletDtoToWallet(walletDto)));
    }

    @Override
    public WalletDto getWalletById(UUID uuid) {
        return walletMapper.walletToWalletDto(walletRepository.findById(uuid).orElseThrow());
    }


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public WalletDto topUpAmountToWallet(TopUpDto topUpDto) {
        Wallet wallet = walletRepository.findById(topUpDto.getWalletId()).orElseThrow();

        topUpService.charge(topUpDto);
        topUpService.saveTopUp(topUpDto, wallet);

        wallet.setBalance(wallet.getBalance().add(topUpDto.getAmount()));
        walletRepository.saveAndFlush(wallet);

        return walletMapper.walletToWalletDto(wallet);
    }


}
