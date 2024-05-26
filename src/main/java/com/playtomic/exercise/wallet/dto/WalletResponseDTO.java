package com.playtomic.exercise.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for wallet responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {
  /**
   * The unique identifier for the wallet.
   */
  private UUID id;

  /**
   * The owner of the wallet.
   */
  private String owner;

  /**
   * The balance of the wallet.
   */
  private BigDecimal balance;
}
