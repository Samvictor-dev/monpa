package com.myvamsnet.monpa.common.exception;

import com.myvamsnet.monpa.common.valueobject.Money;

public class InsufficientFundsException
        extends RuntimeException {

    public InsufficientFundsException(
            String accountNumber,
            Money amount) {

        super(
                "Wallet " + accountNumber +
                        " has insufficient funds for " +
                        amount
        );
    }
}
