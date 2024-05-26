package com.playtomic.exercise.wallet.service;

import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Interface for managing wallet operations.
 */
public interface IWalletService {
  /**
   * Retrieves a wallet by its ID.
   *
   * @param walletId the ID of the wallet to retrieve
   * @return the {@link WalletResponseDTO} containing the wallet details
   */
  WalletResponseDTO getWalletById(UUID walletId);

  /**
   * Tops up the wallet with the specified amount.
   *
   * @param walletId the ID of the wallet to top up
   * @param creditCardNumber the credit card number to charge
   * @param amount the amount to top up
   * @return the {@link WalletResponseDTO} containing the updated wallet details
   */
  WalletResponseDTO topUpWallet(
    UUID walletId,
    String creditCardNumber,
    BigDecimal amount
  );
}
