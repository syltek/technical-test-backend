package com.playtomic.anemone.technical_test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Refund {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("payment_id")
    private UUID paymentId;

    @JsonProperty("amount")
    private BigDecimal amount;
}
