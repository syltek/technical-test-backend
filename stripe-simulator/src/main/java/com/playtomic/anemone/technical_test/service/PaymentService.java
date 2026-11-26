package com.playtomic.anemone.technical_test.service;

import com.playtomic.anemone.technical_test.domain.Charge;
import com.playtomic.anemone.technical_test.domain.Payment;
import com.playtomic.anemone.technical_test.domain.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {

    @Nonnull
    private static BigDecimal MINIMUM_CHARGE = BigDecimal.TEN;

    @Nonnull
    private Map<UUID, BigDecimal> chargesMap = new ConcurrentHashMap<>();

    @Nonnull
    public Mono<Payment> charge(@Nonnull Charge charge) {
        if (charge.getAmount().compareTo(MINIMUM_CHARGE) < 0) {
            return Mono.error(new TooSmallAmountException());
        }

        UUID paymentId = UUID.randomUUID();
        chargesMap.put(paymentId, charge.getAmount());

        Payment p = new Payment(paymentId, charge.getAmount());
        log.info("New payment: {} {}", p.getId(), p.getAmount());

        return Mono.just(p);
    }


    @Nonnull
    public Mono<Refund> refund(@Nonnull UUID paymentId) {
        BigDecimal chargedAmount = chargesMap.remove(paymentId);
        if (chargedAmount == null) {
            return Mono.error(new ChargeNotFoundException());
        }

        UUID refundId = UUID.randomUUID();

        Refund r = new Refund(refundId, paymentId, chargedAmount);
        log.info("New refund: {} {} {}", r.getId(), r.getPaymentId(), r.getAmount());

        return Mono.just(r);
    }
}
