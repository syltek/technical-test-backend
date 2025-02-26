package com.playtomic.tests.wallet.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class TopUpRequest {

    @NotBlank(message = "Please provide a credit card number")
    private String creditCardNumber;

    @Min(value=0,message = "Please provide a positive amount")
    private BigDecimal amount;
}
