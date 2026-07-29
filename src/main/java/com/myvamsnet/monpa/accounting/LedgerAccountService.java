package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.common.exception.LedgerAccountNotFoundException;
import com.myvamsnet.monpa.model.LedgerAccount;
import com.myvamsnet.monpa.model.LedgerAccountType;
import com.myvamsnet.monpa.repository.LedgerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LedgerAccountService {

    private final LedgerAccountRepository ledgerAccountRepository;

    public LedgerAccount getRequiredAccount(LedgerAccountType type) {

        return ledgerAccountRepository
                .findByType(type)
                .orElseThrow(() ->
                        new LedgerAccountNotFoundException(type));

    }

}
