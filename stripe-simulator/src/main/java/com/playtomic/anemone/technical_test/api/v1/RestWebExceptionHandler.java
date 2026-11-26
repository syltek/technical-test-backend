package com.playtomic.anemone.technical_test.api.v1;

import com.playtomic.anemone.technical_test.service.ChargeNotFoundException;
import com.playtomic.anemone.technical_test.service.TooSmallAmountException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class RestWebExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable ex) {
        if (ex instanceof ChargeNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);

            // marks the response as complete and forbids writing to it
            return exchange.getResponse().setComplete();
        }

        if (ex instanceof TooSmallAmountException) {
            exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);

            // marks the response as complete and forbids writing to it
            return exchange.getResponse().setComplete();
        }

        return Mono.error(ex);
    }
}
