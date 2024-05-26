package com.playtomic.exercise.wallet.service;

import com.playtomic.exercise.payment.provider.stripe.service.StripeService;
import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import com.playtomic.exercise.wallet.exception.WalletNotFoundException;
import com.playtomic.exercise.wallet.model.Wallet;
import com.playtomic.exercise.wallet.model.WalletMapper;
import com.playtomic.exercise.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing wallet operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService implements IWalletService {
  private final WalletRepository walletRepository;
  private final StripeService stripeService;
  private final WalletMapper walletMapper;

  @Override
  public WalletResponseDTO getWalletById(final UUID walletId) {
    log.info("Fetching wallet with ID: {}", walletId);

    final Wallet wallet = walletRepository
      .findById(walletId)
      .orElseThrow(
        () -> {
          final String message = String.format(
            "Wallet not found for ID: %s",
            walletId
          );
          log.error(message);
          return new WalletNotFoundException(message);
        }
      );

    final WalletResponseDTO response = walletMapper.walletToWalletResponseDTO(
      wallet
    );

    log.info("Wallet fetched successfully for ID: {}: {}", walletId, response);
    return response;
  }

  @Override
  @Transactional
  public WalletResponseDTO topUpWallet(
    @NonNull final UUID walletId,
    @NonNull final String creditCardNumber,
    @NonNull final BigDecimal amount
  ) {
    log.info("Topping up wallet with ID: {} with amount: {}", walletId, amount);
    final Wallet wallet = walletRepository
      .findByIdWithLock(walletId)
      .orElseThrow(
        () -> {
          final String message = String.format(
            "Wallet not found for ID: %s",
            walletId
          );
          log.error(message);
          return new WalletNotFoundException(message);
        }
      );

    log.info("Charging credit card for wallet ID: {}", walletId);
    stripeService.charge(creditCardNumber, amount);

    wallet.setBalance(wallet.getBalance().add(amount));
    final Wallet updatedWallet = walletRepository.save(wallet);
    final WalletResponseDTO response = walletMapper.walletToWalletResponseDTO(
      updatedWallet
    );

    log.info(
      "Wallet topped up successfully for ID: {}: {}",
      walletId,
      response
    );
    return response;
  }
}
