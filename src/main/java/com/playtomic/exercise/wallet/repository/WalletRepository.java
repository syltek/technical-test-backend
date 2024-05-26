package com.playtomic.exercise.wallet.repository;

import com.playtomic.exercise.wallet.model.Wallet;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Wallet} entities.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
  /**
   * Finds a wallet by its ID using a pessimistic write lock.
   *
   * @param walletId the ID of the wallet
   * @return an {@link Optional} containing the wallet if found, or empty if not found
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT w FROM Wallet w WHERE w.id = :walletId")
  Optional<Wallet> findByIdWithLock(UUID walletId);
}
