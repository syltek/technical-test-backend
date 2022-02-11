package com.playtomic.tests.wallet.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StripeChargeResponse {

    @NonNull
    @JsonProperty("id")
    String paymentId;

    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;
}
