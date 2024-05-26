package com.playtomic.exercise.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.playtomic.exercise.payment.provider.stripe.service.StripeService;
import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import com.playtomic.exercise.wallet.exception.WalletNotFoundException;
import com.playtomic.exercise.wallet.model.Wallet;
import com.playtomic.exercise.wallet.model.WalletMapper;
import com.playtomic.exercise.wallet.repository.WalletRepository;
import jakarta.persistence.PessimisticLockException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
  @Mock
  private WalletRepository walletRepository;

  @Mock
  private StripeService stripeService;

  @Mock
  private WalletMapper walletMapper;

  @InjectMocks
  private WalletService walletService;

  @Test
  @DisplayName("Test successful retrieval of wallet by ID")
  void testGetWalletById_Success() {
    final UUID walletId = UUID.randomUUID();
    final Wallet wallet = Wallet
      .builder()
      .id(walletId)
      .balance(BigDecimal.valueOf(200))
      .build();
    final WalletResponseDTO walletResponseDTO = WalletResponseDTO
      .builder()
      .id(walletId)
      .balance(wallet.getBalance())
      .build();

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
    when(walletMapper.walletToWalletResponseDTO(wallet))
      .thenReturn(walletResponseDTO);

    final WalletResponseDTO response = walletService.getWalletById(walletId);
    assertNotNull(response);
    assertEquals(walletId, response.getId());
    assertEquals(wallet.getBalance(), response.getBalance());

    verify(walletRepository).findById(walletId);
    verify(walletMapper).walletToWalletResponseDTO(wallet);
  }

  @Test
  @DisplayName("Test retrieval of wallet by ID when wallet is not found")
  void testGetWalletById_WalletNotFound() {
    final UUID walletId = UUID.randomUUID();

    when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

    final WalletNotFoundException exception = assertThrows(
      WalletNotFoundException.class,
      () -> walletService.getWalletById(walletId)
    );

    assertEquals(
      "Wallet not found for ID: " + walletId,
      exception.getMessage()
    );

    verify(walletRepository).findById(walletId);
    verify(walletMapper, never()).walletToWalletResponseDTO(any(Wallet.class));
  }

  @Test
  @DisplayName("Test successful top-up of wallet")
  void testTopUpWallet_Success() {
    final UUID walletId = UUID.randomUUID();
    final BigDecimal amount = BigDecimal.valueOf(100);
    final Wallet wallet = Wallet
      .builder()
      .id(walletId)
      .balance(BigDecimal.valueOf(200))
      .build();

    final WalletResponseDTO walletResponseDTO = WalletResponseDTO
      .builder()
      .id(walletId)
      .balance(wallet.getBalance().add(amount))
      .build();
    final String creditCardNumber = "4111111111111111";

    when(walletRepository.findByIdWithLock(walletId))
      .thenReturn(Optional.of(wallet));
    when(walletRepository.save(wallet)).thenReturn(wallet);
    when(walletMapper.walletToWalletResponseDTO(wallet))
      .thenReturn(walletResponseDTO);

    final WalletResponseDTO response = walletService.topUpWallet(
      walletId,
      creditCardNumber,
      amount
    );

    assertNotNull(response);
    assertEquals(walletId, response.getId());
    assertEquals(BigDecimal.valueOf(300), response.getBalance());

    verify(walletRepository).findByIdWithLock(walletId);
    verify(stripeService).charge(creditCardNumber, amount);
    verify(walletRepository).save(wallet);
    verify(walletMapper).walletToWalletResponseDTO(wallet);
  }

  @Test
  @DisplayName("Test top-up of wallet when wallet is not found")
  void testTopUpWallet_WalletNotFound() {
    final UUID walletId = UUID.randomUUID();
    final BigDecimal amount = BigDecimal.valueOf(100);
    final String creditCardNumber = "4111111111111111";

    when(walletRepository.findByIdWithLock(walletId))
      .thenReturn(Optional.empty());

    final WalletNotFoundException exception = assertThrows(
      WalletNotFoundException.class,
      () -> walletService.topUpWallet(walletId, creditCardNumber, amount)
    );

    assertEquals(
      "Wallet not found for ID: " + walletId,
      exception.getMessage()
    );

    verify(walletRepository).findByIdWithLock(walletId);
    verify(stripeService, never()).charge(anyString(), any(BigDecimal.class));
    verify(walletRepository, never()).save(any(Wallet.class));
    verify(walletMapper, never()).walletToWalletResponseDTO(any(Wallet.class));
  }

  @Test
  @DisplayName("Test top-up of wallet when PessimisticLockException occurs")
  void testTopUpWallet_PessimisticLockException() {
    final UUID walletId = UUID.randomUUID();
    final BigDecimal amount = BigDecimal.valueOf(100);
    final String creditCardNumber = "4111111111111111";

    when(walletRepository.findByIdWithLock(walletId))
      .thenThrow(new PessimisticLockException("Could not acquire lock"));

    final PessimisticLockException exception = assertThrows(
      PessimisticLockException.class,
      () -> walletService.topUpWallet(walletId, creditCardNumber, amount)
    );

    assertEquals("Could not acquire lock", exception.getMessage());

    verify(walletRepository).findByIdWithLock(walletId);
    verify(stripeService, never()).charge(anyString(), any(BigDecimal.class));
    verify(walletRepository, never()).save(any(Wallet.class));
    verify(walletMapper, never()).walletToWalletResponseDTO(any(Wallet.class));
  }
}
