package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    @NonNull
    @JsonProperty("id")
    private String id;

    @NonNull
    @JsonProperty("walletId")
    private Long walletId;

    @NonNull
    @JsonProperty("amount")
    private BigDecimal amount;

    @NonNull
    @JsonProperty("createdAt")
    private Date createdAt;

    @NonNull
    @JsonProperty("isRefunded")
    @Builder.Default
    private boolean isRefunded = false;
}
