package com.myvamsnet.monpa.common.exception;

public class WalletNotFoundException extends ResourceNotFoundException {

    public WalletNotFoundException(String accountNumber) {
        super("Wallet with account number '" + accountNumber + "' was not found.");
    }

    public WalletNotFoundException(Long userId) {
        super("Wallet for user ID " + userId + " was not found.");
    }
}
