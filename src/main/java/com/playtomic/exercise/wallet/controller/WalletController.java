package com.playtomic.exercise.wallet.controller;

import com.playtomic.exercise.wallet.dto.TopUpRequestDTO;
import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import com.playtomic.exercise.wallet.service.WalletService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing wallet operations.
 */
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  /**
   * Retrieves a wallet by its ID.
   *
   * @param id the ID of the wallet to retrieve
   * @return the {@link WalletResponseDTO} containing the wallet details
   */
  @GetMapping("/{id}")
  public WalletResponseDTO getWalletById(@PathVariable UUID id) {
    return walletService.getWalletById(id);
  }

  /**
   * Tops up the wallet with the specified amount.
   *
   * @param topUpRequest the {@link TopUpRequestDTO} containing the top-up details
   * @return the {@link WalletResponseDTO} containing the updated wallet details
   */
  @PostMapping("/top-up")
  public WalletResponseDTO topUpWallet(
    @Valid @RequestBody TopUpRequestDTO topUpRequest
  ) {
    return walletService.topUpWallet(
      topUpRequest.getWalletId(),
      topUpRequest.getCreditCardNumber(),
      topUpRequest.getAmount()
    );
  }
}
