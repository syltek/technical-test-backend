package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripePaymentDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("amount")
    private String amount;
}
