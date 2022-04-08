package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.service.StripeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class StripeServiceTestConfiguration {
    @Bean
    @Primary
    public StripeService nameService() {
        return Mockito.mock(StripeService.class);
    }
}
