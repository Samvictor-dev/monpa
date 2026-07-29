package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final JournalService journalService;
    private final LedgerAccountService ledgerAccountService;

    @Transactional
    public Journal recordDeposit(
            Wallet wallet,
            Money money,
            String narration
    ) {

        Journal journal = journalService.createJournal(
                JournalType.DEPOSIT,
                money,
                narration
        );

        LedgerAccount cash =
                ledgerAccountService.getRequiredAccount(
                        LedgerAccountType.CASH
                );

        LedgerAccount liability =
                ledgerAccountService.getRequiredAccount(
                        LedgerAccountType.CUSTOMER_LIABILITY
                );

        journalService.createDebitEntry(
                journal,
                cash,
                money,
                "Cash received"
        );

        journalService.createCreditEntry(
                journal,
                liability,
                money,
                "Customer wallet funded"
        );

        journalService.validateAndPost(journal);

        return journal;
    }

    @Transactional
    public Journal recordWithdrawal(
            Wallet wallet,
            Money money,
            String narration
    ) {

        Journal journal = journalService.createJournal(
                JournalType.WITHDRAWAL,
                money,
                narration
        );

        LedgerAccount liability =
                ledgerAccountService.getRequiredAccount(
                        LedgerAccountType.CUSTOMER_LIABILITY
                );

        LedgerAccount cash =
                ledgerAccountService.getRequiredAccount(
                        LedgerAccountType.CASH
                );

        journalService.createDebitEntry(
                journal,
                liability,
                money,
                "Customer withdrawal"
        );

        journalService.createCreditEntry(
                journal,
                cash,
                money,
                "Cash paid out"
        );

        journalService.validateAndPost(journal);

        return journal;
    }

    @Transactional
    public Journal recordTransfer(
            Wallet sender,
            Wallet receiver,
            Money money,
            String narration
    ) {

        Journal journal = journalService.createJournal(
                JournalType.TRANSFER,
                money,
                narration
        );

        LedgerAccount liability =
                ledgerAccountService.getRequiredAccount(
                        LedgerAccountType.CUSTOMER_LIABILITY
                );

        journalService.createDebitEntry(
                journal,
                liability,
                money,
                "Transfer from " + sender.getAccountNumber()
        );

        journalService.createCreditEntry(
                journal,
                liability,
                money,
                "Transfer to " + receiver.getAccountNumber()
        );

        journalService.validateAndPost(journal);

        return journal;
    }

}
