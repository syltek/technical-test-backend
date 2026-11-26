package com.playtomic.anemone.technical_test.api.v1;

import com.playtomic.anemone.technical_test.domain.Charge;
import com.playtomic.anemone.technical_test.domain.Payment;
import com.playtomic.anemone.technical_test.domain.Refund;
import com.playtomic.anemone.technical_test.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class StripeSimulatorReactiveControllerV1 {

    @Nonnull
    final private PaymentService paymentService;

    @PostMapping(path = "/v1/stripe-simulator/charges", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Nonnull
    public Mono<Payment> charge(@RequestBody @Nonnull Charge charge) {
        Mono<Payment> m = paymentService.charge(charge);

        return m;
    }

    @PostMapping(path = "/v1/stripe-simulator/payments/{payment_id}/refunds", produces = MediaType.APPLICATION_JSON_VALUE)
    @Nonnull
    public Mono<Refund> refund(@PathVariable("payment_id") @Nonnull UUID paymentId) {
        Mono<Refund> refund = paymentService.refund(paymentId);

        return refund;
    }
}
