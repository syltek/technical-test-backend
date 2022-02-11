package com.playtomic.tests.wallet.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopUpDto {
    private UUID id;
    private UUID walletId;
    private BigDecimal amount;
    private String creditCardNumber;
    private String paymentId;
}
