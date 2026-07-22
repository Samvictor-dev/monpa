package com.myvamsnet.monpa.common.exception;

public class CurrencyMismatchException extends RuntimeException {

    public CurrencyMismatchException() {
        super("Wallet currencies do not match.");
    }
}
