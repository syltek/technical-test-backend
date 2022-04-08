package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.service.WalletBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceHistoryRepository extends JpaRepository<WalletBalanceHistory, Long> {
}
