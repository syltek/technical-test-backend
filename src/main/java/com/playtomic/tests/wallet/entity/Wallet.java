package com.playtomic.tests.wallet.entity;

import com.playtomic.tests.wallet.enums.Currency;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Wallet")
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Builder.Default
    private BigDecimal balance = BigDecimal.valueOf(0);
    @NonNull
    private String iban;
    @Version
    private Long version;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
}
