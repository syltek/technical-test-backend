package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.exception.ResourceNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet getWallet(String id) {
        return this.walletRepository
                .findById(id)
                .map(this::convertToWallet)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource not found: %s", id)));
    }

    private Wallet convertToWallet(WalletEntity walletEntity) {
        return new Wallet(walletEntity.getId(),walletEntity.getBalance());
    }
}
