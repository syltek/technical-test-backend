package com.playtomic.exercise.wallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a Wallet.
 */
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
  /**
   * The unique identifier for the wallet.
   */
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  /**
   * The owner of the wallet.
   */
  private String owner;

  /**
   * The balance of the wallet.
   */
  private BigDecimal balance;

  /**
   * The timestamp when the wallet was created.
   */
  @CreationTimestamp
  private Instant creationTimestamp;

  /**
   * The timestamp when the wallet was last updated.
   */
  @UpdateTimestamp
  private Instant updateTimestamp;
}
