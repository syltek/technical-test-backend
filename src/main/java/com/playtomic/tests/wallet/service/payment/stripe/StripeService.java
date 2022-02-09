package com.playtomic.tests.wallet.service.payment.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.dto.StripePaymentDto;
import com.playtomic.tests.wallet.service.payment.PaymentService;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 * Updated by Orkun Cavdar on 08/02/2022
 */
@Service
public class StripeService implements PaymentService {

    private static final Log log = LogFactory.getLog(StripeService.class);

    @NonNull
    private final String chargesUri;

    @NonNull
    private final String refundsUri;

    @NonNull
    private final RestTemplate restTemplate;

    public StripeService(@Value("${stripe.simulator.charges-uri}") @NonNull String chargesUri,
                         @Value("${stripe.simulator.refunds-uri}") @NonNull String refundsUri,
                         @NotNull RestTemplateBuilder restTemplateBuilder) {
        this.chargesUri = chargesUri;
        this.refundsUri = refundsUri;
        this.restTemplate =
                restTemplateBuilder
                        .errorHandler(new StripeRestTemplateResponseErrorHandler())
                        .build();
    }

    /**
     * Charges money in the credit card.
     * <p>
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount           The amount that will be charged.
     * @throws StripeServiceException
     */
    public StripePaymentDto charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        // Object.class because we don't read the body here.
        return restTemplate.postForObject(chargesUri, body, StripePaymentDto.class);
    }

    /**
     * Refunds the specified payment.
     */
    public void refund(@NonNull String paymentId) throws StripeServiceException {
        // Object.class because we don't read the body here.
        restTemplate.postForEntity(refundsUri.replace("{payment_id}", paymentId), null, Object.class, paymentId);
    }

    @AllArgsConstructor
    private static class ChargeRequest {

        @NonNull
        @JsonProperty("credit_card")
        String creditCardNumber;

        @NonNull
        @JsonProperty("amount")
        BigDecimal amount;
    }
}
