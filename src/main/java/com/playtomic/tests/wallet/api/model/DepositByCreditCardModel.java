package com.playtomic.tests.wallet.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class DepositByCreditCardModel {
    BigDecimal amount;
    String creditCard;
}
