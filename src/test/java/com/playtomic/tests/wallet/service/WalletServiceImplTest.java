package com.playtomic.tests.wallet.service;

import static org.mockito.Mockito.doReturn;

import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.exception.ResourceNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void getWallet_shouldReturnWallet() {
        var id = "wallet_id";
        var walletEntity = new WalletEntity(id,987665);
        doReturn(Optional.of(walletEntity)).when(walletRepository).findById(id);

        var result = walletService.getWallet(id);

        assertNotNull(result);
        assertEquals(walletEntity.getId(), result.getId());
        assertEquals(walletEntity.getBalance(), result.getBalance());
    }

    @Test
    void getWallet_shouldThrowExceptionWhenWalletNotFound() {
        var id = "wallet_id_not_found";
        doReturn(Optional.empty()).when(walletRepository).findById(id);

        assertThrows(ResourceNotFoundException.class, () -> walletService.getWallet(id));
    }
}