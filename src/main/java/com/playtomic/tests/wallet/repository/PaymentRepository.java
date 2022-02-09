package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Payment;
import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT p FROM Payment p WHERE p.walletId = ?1")
    List<Payment> findByWalletId(Long id);
}
