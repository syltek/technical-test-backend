package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.exception.ResourceNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;

    public WalletServiceImpl(WalletRepository walletRepository, PaymentService paymentService) {
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
    }

    @Override
    public Wallet getWallet(String id) {
        return this.walletRepository
                .findById(id)
                .map(this::convertToWallet)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource not found: %s", id)));
    }

    @Override
    public Wallet topUp(String walletId, String creditCardNumber, BigDecimal amount) {
        var walletEntity = this.walletRepository
                .findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource not found: %s", walletId)));

        //storing the charge history could be another enhancement
        //if there is no exception thrown we assume payment is successful as we don´t have more information on the response
        this.paymentService.charge(creditCardNumber,amount);

        walletEntity.setBalance(walletEntity.getBalance()+amount.doubleValue());

        return convertToWallet(this.walletRepository.save(walletEntity));
    }

    private Wallet convertToWallet(WalletEntity walletEntity) {
        return new Wallet(walletEntity.getId(),walletEntity.getBalance());
    }
}
