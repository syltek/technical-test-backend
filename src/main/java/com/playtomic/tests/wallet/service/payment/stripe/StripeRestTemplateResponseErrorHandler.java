package com.playtomic.tests.wallet.service.payment.stripe;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Updated by Orkun Cavdar on 08/02/2022
 */
public class StripeRestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        if (statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
            throw new StripeAmountTooSmallException("Amount is too small to charge");
        }

        super.handleError(response, statusCode);
    }
}
