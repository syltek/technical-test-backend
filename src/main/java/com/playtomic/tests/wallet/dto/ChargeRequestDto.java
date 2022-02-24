package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class ChargeRequestDto extends RequestDto{

    @NonNull
    @JsonProperty("currency")
    private String currency;

    @NonNull
    @JsonProperty("amount")
    private BigDecimal amount;

    @NonNull
    @JsonProperty("creditCardNumber")
    private String creditCardNumber;

    @Override
    public String toString() {
        return "ChargeRequestDto{" +
                "currency='" + this.getCurrency() + '\'' +
                ", amount=" + this.getAmount() +
                ", creditCardNumber='" + this.getCreditCardNumber() + '\'' +
                '}';
    }
}
