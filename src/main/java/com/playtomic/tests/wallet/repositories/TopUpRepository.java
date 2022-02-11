package com.playtomic.tests.wallet.repositories;

import com.playtomic.tests.wallet.domain.TopUp;
import com.playtomic.tests.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TopUpRepository extends JpaRepository<TopUp, UUID> {
    List<TopUp> findAllByWallet(Wallet wallet);
}
