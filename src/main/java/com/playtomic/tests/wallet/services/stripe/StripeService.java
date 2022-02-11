package com.playtomic.tests.wallet.services.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.web.model.StripeChargeResponse;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;


/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
public class StripeService {

    private final ObjectMapper mapper = new ObjectMapper();

    @NonNull
    private final URI chargesUri;
    //"id" -> "0bc5c554-cb49-4bcd-99c0-d282f9543ab5"
//        "amount" -> {Integer@4312} 15
    @NonNull
    private final URI refundsUri;

    @NonNull
    private final RestTemplate restTemplate;

    public StripeService(@Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
                         @Value("${stripe.simulator.refunds-uri}") @NonNull URI refundsUri,
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
     * @throws StripeServiceException the exception when an error occured in service and amount is less than 10
     */
    public StripeChargeResponse charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        // Object.class because we don't read the body here.
        Object o = restTemplate.postForObject(chargesUri, body, Object.class);
        return mapper.convertValue(o, StripeChargeResponse.class);
    }

    /**
     * Refunds the specified payment.
     */
    public void refund(@NonNull String paymentId) throws StripeServiceException {
        // Object.class because we don't read the body here.
        String refundRequest = appendPaymentIdToRefund(paymentId);
        URI refundUri = URI.create(refundRequest);
        restTemplate.postForEntity(refundUri, null, Object.class);
    }

    private String appendPaymentIdToRefund(String paymentId) {
        return refundsUri.toString().replace("%7Bpayment_id%7D", paymentId);
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
