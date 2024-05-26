package com.playtomic.exercise.wallet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.playtomic.exercise.wallet.model.Wallet;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class WalletRepositoryIT {
  @Autowired
  private WalletRepository walletRepository;

  @Test
  @Transactional
  void testFindByIdWithLock() {
    final Wallet wallet = Wallet
      .builder()
      .owner("Test Owner")
      .balance(BigDecimal.ZERO)
      .build();
    final UUID walletId = walletRepository.save(wallet).getId();

    final Optional<Wallet> savedWallet = walletRepository.findByIdWithLock(
      walletId
    );

    assertThat(savedWallet.get().getId()).isEqualTo(wallet.getId());
  }
}
