package com.myvamsnet.monpa.accounting.validator;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.model.LedgerEntry;
import com.myvamsnet.monpa.model.LedgerEntryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JournalValidator {

    public void validate(Journal journal) {

        // 1. Journal must exist
        if (journal == null) {

            throw new IllegalArgumentException(
                    "Journal cannot be null."
            );
        }


        // 2. Journal must have entries
        List<LedgerEntry> entries =
                journal.getEntries();

        if (entries == null || entries.isEmpty()) {

            throw new IllegalStateException(
                    "Journal must contain at least one ledger entry."
            );
        }


        BigDecimal debitTotal =
                BigDecimal.ZERO;

        BigDecimal creditTotal =
                BigDecimal.ZERO;


        // 3. Validate every entry
        for (LedgerEntry entry : entries) {

            validateEntry(journal, entry);


            Money money =
                    entry.getMoney();


            if (entry.getEntryType()
                    == LedgerEntryType.DEBIT) {

                debitTotal =
                        debitTotal.add(
                                money.getAmount()
                        );

            }

            else if (
                    entry.getEntryType()
                            == LedgerEntryType.CREDIT
            ) {

                creditTotal =
                        creditTotal.add(
                                money.getAmount()
                        );

            }

            else {

                throw new IllegalStateException(
                        "Ledger entry has an invalid entry type."
                );
            }
        }


        // 4. Journal must balance
        if (debitTotal.compareTo(creditTotal) != 0) {

            throw new IllegalStateException(
                    "Journal is not balanced. " +
                            "Debit: " + debitTotal +
                            ", Credit: " + creditTotal
            );
        }


        // 5. Journal amount must agree
        //    with the debit side
        if (journal.getAmount() == null) {

            throw new IllegalStateException(
                    "Journal amount cannot be null."
            );
        }


        if (journal.getAmount()
                .compareTo(debitTotal) != 0) {

            throw new IllegalStateException(
                    "Journal amount does not match " +
                            "the total debit amount. " +
                            "Journal amount: "
                            + journal.getAmount()
                            + ", Debit total: "
                            + debitTotal
            );
        }
    }


    private void validateEntry(
            Journal journal,
            LedgerEntry entry
    ) {

        // Entry must exist
        if (entry == null) {

            throw new IllegalStateException(
                    "Journal contains a null ledger entry."
            );
        }


        // Entry must belong to this journal
        if (entry.getJournal() != journal) {

            throw new IllegalStateException(
                    "Ledger entry does not belong " +
                            "to the journal being validated."
            );
        }


        // Ledger account is required
        if (entry.getLedgerAccount() == null) {

            throw new IllegalStateException(
                    "Ledger entry must have a ledger account."
            );
        }


        // Money is required
        if (entry.getMoney() == null) {

            throw new IllegalStateException(
                    "Ledger entry money cannot be null."
            );
        }


        // Amount must be positive
        if (!entry.getMoney().isPositive()) {

            throw new IllegalStateException(
                    "Ledger entry amount must be greater than zero."
            );
        }


        // Entry type is required
        if (entry.getEntryType() == null) {

            throw new IllegalStateException(
                    "Ledger entry type cannot be null."
            );
        }


        // Currency must match journal currency
        if (journal.getCurrency() == null) {

            throw new IllegalStateException(
                    "Journal currency cannot be null."
            );
        }


        if (!journal.getCurrency().equals(
                entry.getMoney()
                        .getCurrency()
                        .name()
        )) {

            throw new IllegalStateException(
                    "Ledger entry currency does not " +
                            "match journal currency."
            );
        }
    }
}
