package com.playtomic.exercise.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for top-up requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequestDTO {
  /**
   * The ID of the wallet to top up.
   */
  @NotNull(message = "Wallet ID cannot be null")
  private UUID walletId;

  /**
   * The credit card number to charge.
   */
  @NotBlank(message = "Credit card number cannot be blank")
  @Size(
    min = 16,
    max = 19,
    message = "Credit card number must be between 16 and 19 characters"
  )
  private String creditCardNumber;

  /**
   * The amount to top up.
   */
  @NotNull(message = "Amount cannot be null")
  @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
  private BigDecimal amount;
}
