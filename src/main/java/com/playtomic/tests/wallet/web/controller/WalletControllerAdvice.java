package com.playtomic.tests.wallet.web.controller;

import com.playtomic.tests.wallet.services.stripe.StripeAmountTooSmallException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class WalletControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List> validationErrorHandler(ConstraintViolationException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeAmountTooSmallException.class)
    public ResponseEntity<List> stripeErrorHandler(StripeAmountTooSmallException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<List> noSuchElementHandler(NoSuchElementException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<List> optimisticLockingFailureException(NoSuchElementException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

}
