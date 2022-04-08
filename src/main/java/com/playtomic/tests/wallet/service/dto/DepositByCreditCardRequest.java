package com.playtomic.tests.wallet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositByCreditCardRequest {

    long walletId;
    String creditCard;
    BigDecimal amount;
}
