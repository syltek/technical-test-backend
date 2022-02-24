package com.playtomic.tests.wallet.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "Payment")
public class Payment {
    @Id
    private String id;
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
