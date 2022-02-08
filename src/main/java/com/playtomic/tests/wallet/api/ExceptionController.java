package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.ExceptionResponseDto;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import com.playtomic.tests.wallet.service.payment.stripe.StripeAmountTooSmallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(StripeAmountTooSmallException.class)
    public ResponseEntity<ExceptionResponseDto> handleStripeAmountTooSmallException(Exception e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // 422
        return new ResponseEntity<>(
                new ExceptionResponseDto(status.name(), status.value(), e.getMessage()), status
        );
    }
    @ExceptionHandler(WalletResponseException.class)
    public ResponseEntity<ExceptionResponseDto> handleWalletResponseException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        return new ResponseEntity<>(
                new ExceptionResponseDto(status.name(), status.value(), e.getMessage()), status
        );
    }
}
