package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDto {
    @NonNull
    @JsonProperty("id")
    private Long id;

    @NonNull
    @JsonProperty("currency")
    private Currency currency;

    @NonNull
    @JsonProperty("balance")
    private BigDecimal balance;

    @NonNull
    @JsonProperty("iban")
    private String iban;

    @JsonProperty("version")
    private Long version;

    @NonNull
    @JsonProperty("createdAt")
    private Date createdAt;

    @NonNull
    @JsonProperty("updatedAt")
    private Date updatedAt;

    @NonNull
    @JsonProperty("paymentIds")
    private List<String> paymentIds = new ArrayList<>();
}
