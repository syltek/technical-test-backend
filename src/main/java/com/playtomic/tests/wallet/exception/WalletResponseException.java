package com.playtomic.tests.wallet.exception;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
public class WalletResponseException extends RuntimeException {
    public WalletResponseException(String message) {
        super(message);
    }
}
