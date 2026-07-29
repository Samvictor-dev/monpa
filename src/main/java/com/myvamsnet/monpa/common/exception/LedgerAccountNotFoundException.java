package com.myvamsnet.monpa.common.exception;

import com.myvamsnet.monpa.model.LedgerAccountType;

public class LedgerAccountNotFoundException extends RuntimeException {

    public LedgerAccountNotFoundException(LedgerAccountType accountType) {
        super("Ledger account not found for type: " + accountType);
    }

}
