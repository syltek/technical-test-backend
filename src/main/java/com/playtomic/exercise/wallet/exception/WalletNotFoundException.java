package com.playtomic.exercise.wallet.exception;

/**
 * Exception thrown when a wallet is not found.
 */
public class WalletNotFoundException extends RuntimeException {

  /**
   * Constructs a new WalletNotFoundException with the specified detail message.
   *
   * @param message the detail message
   */
  public WalletNotFoundException(String message) {
    super(message);
  }
}
