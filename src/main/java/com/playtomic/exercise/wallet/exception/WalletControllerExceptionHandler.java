package com.playtomic.exercise.wallet.exception;

import com.playtomic.exercise.payment.provider.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.exercise.wallet.controller.WalletController;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for {@link WalletController}.
 */
@Slf4j
@ControllerAdvice(assignableTypes = { WalletController.class })
public class WalletControllerExceptionHandler {

  /**
   * Handles {@link WalletNotFoundException}.
   *
   * @param e the {@link WalletNotFoundException}
   * @return a {@link ResponseEntity} with a {@link HttpStatus#NOT_FOUND} status and the exception
   * message
   */
  @ExceptionHandler(WalletNotFoundException.class)
  public ResponseEntity<String> handleWalletNotFoundException(
    WalletNotFoundException e
  ) {
    log.error("Wallet not found exception: {}", e.getMessage());
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  /**
   * Handles {@link StripeAmountTooSmallException}.
   *
   * @param e the {@link StripeAmountTooSmallException}
   * @return a {@link ResponseEntity} with a {@link HttpStatus#UNPROCESSABLE_ENTITY} status and the
   * exception message
   */
  @ExceptionHandler(StripeAmountTooSmallException.class)
  public ResponseEntity<String> handlePaymentProcessingException(
    StripeAmountTooSmallException e
  ) {
    log.error("Payment processing exception: {}", e.getMessage());
    return new ResponseEntity<>(
      e.getMessage(),
      HttpStatus.UNPROCESSABLE_ENTITY
    );
  }

  /**
   * Handles {@link MethodArgumentNotValidException}.
   *
   * @param ex the {@link MethodArgumentNotValidException}
   * @return a {@link ResponseEntity} with a {@link HttpStatus#BAD_REQUEST} status and a map of
   * field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
    MethodArgumentNotValidException ex
  ) {
    Map<String, String> errors = ex
      .getBindingResult()
      .getAllErrors()
      .stream()
      .collect(
        Collectors.toMap(
          error -> ((FieldError) error).getField(),
          error -> error.getDefaultMessage()
        )
      );
    log.error("Validation exceptions: {}", errors);
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles generic {@link Exception}.
   *
   * @param e the {@link Exception}
   * @return a {@link ResponseEntity} with an {@link HttpStatus#INTERNAL_SERVER_ERROR} status and a
   * generic error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception e) {
    log.error("Unexpected exception: ", e);
    return new ResponseEntity<>(
      "An unexpected error occurred",
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
