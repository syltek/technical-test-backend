package com.playtomic.tests.wallet.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private Long walletId;
    @NonNull
    private BigDecimal amount;
    @CreationTimestamp
    private Date createdAt;
    @NonNull
    @Builder.Default
    private boolean isRefunded = false;
}
