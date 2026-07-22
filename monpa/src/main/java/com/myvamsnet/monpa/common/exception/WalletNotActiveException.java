package com.myvamsnet.monpa.common.exception;

public class WalletNotActiveException extends RuntimeException {

    public WalletNotActiveException(String accountNumber) {
        super("Wallet '" + accountNumber + "' is not active.");
    }
}
