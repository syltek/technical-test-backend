package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exception.ResourceNotFoundException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhancement: create an Error response class with error code and message for example.
 */
@RestControllerAdvice
public class WalletExceptionHandler {
    private Logger log = LoggerFactory.getLogger(WalletExceptionHandler.class);


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }

    @ExceptionHandler(StripeAmountTooSmallException.class)
    public ResponseEntity<?> handlePaymentGatewayException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Amount too small for top up");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errors = new ArrayList<>();

        methodArgumentNotValidException.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerError(Exception exception) {
        log.error("Unexpected exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong, Please try again later");
    }
}
