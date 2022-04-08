package com.playtomic.tests.wallet.service;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        //get userid from jwt or other auth mechanism.
        return Optional.of(1l);
    }
}
