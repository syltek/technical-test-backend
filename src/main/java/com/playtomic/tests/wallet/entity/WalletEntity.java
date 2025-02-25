package com.playtomic.tests.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private double balance;

    public boolean equals(WalletEntity walletEntity) {
        return this.id.equals(walletEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
