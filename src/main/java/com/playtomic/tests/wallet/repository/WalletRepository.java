package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
