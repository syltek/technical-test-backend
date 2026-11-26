package com.playtomic.anemone.technical_test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.math.BigDecimal;
import javax.annotation.Nonnull;

@Getter
public class Charge {

    @Nonnull
    private String creditCard;

    @Nonnull
    private BigDecimal amount;

    public Charge(@JsonProperty(value = "credit_card", required = true) @Nonnull String creditCard,
                  @JsonProperty(value = "amount", required = true) @Nonnull BigDecimal amount) {
        this.creditCard = creditCard;
        this.amount = amount;
    }
}
