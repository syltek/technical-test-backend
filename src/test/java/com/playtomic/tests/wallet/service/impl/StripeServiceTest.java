package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.service.payment.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.payment.stripe.StripeServiceException;
import com.playtomic.tests.wallet.service.payment.stripe.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.net.URI;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
@ActiveProfiles(profiles = "test")
public class StripeServiceTest {

    String testUrl = "https://sandbox.playtomic.io/v1/stripe-simulator/charges";
    StripeService s = new StripeService(testUrl, testUrl, new RestTemplateBuilder());

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }

    @Configuration
    static class Config {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
            return new PropertySourcesPlaceholderConfigurer();
        }

    }
}
