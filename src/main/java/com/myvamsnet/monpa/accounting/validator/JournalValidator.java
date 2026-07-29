package com.myvamsnet.monpa.accounting.validator;

import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.model.LedgerEntry;
import com.myvamsnet.monpa.model.LedgerEntryType;
import com.myvamsnet.monpa.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JournalValidator {

    private final LedgerEntryRepository ledgerEntryRepository;

    public void validate(Journal journal) {

        // We'll improve this shortly.

        List<LedgerEntry> entries =
                ledgerEntryRepository.findByJournalId(
                        journal.getId()
                );

        BigDecimal debit = BigDecimal.ZERO;

        BigDecimal credit = BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {

            if (entry.getEntryType() == LedgerEntryType.DEBIT) {

                debit = debit.add(
                        entry.getMoney().getAmount()
                );

            } else {

                credit = credit.add(
                        entry.getMoney().getAmount()
                );

            }

        }

        if (debit.compareTo(credit) != 0) {

            throw new IllegalStateException(
                    "Journal is not balanced."
            );

        }

    }

}
