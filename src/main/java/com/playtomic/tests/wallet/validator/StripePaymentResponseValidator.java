package com.playtomic.tests.wallet.validator;

import com.playtomic.tests.wallet.dto.RequestDto;
import com.playtomic.tests.wallet.dto.StripePaymentDto;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StripePaymentResponseValidator implements RequestValidator<RequestDto, StripePaymentDto> {
    @Override
    public void validate(RequestDto requestDto, StripePaymentDto stripePaymentDto) {
        if (Objects.isNull(stripePaymentDto.getId()) || stripePaymentDto.getId().isBlank()) {
            throw new WalletResponseException("Invalid payment id in Stripe Payment Response");
        }
        if (Objects.isNull(stripePaymentDto.getAmount()) || stripePaymentDto.getAmount().isBlank()) {
            throw new WalletResponseException("Invalid payment amount in Stripe Payment Resoonse");
        }
    }
}
