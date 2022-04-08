package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.service.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
