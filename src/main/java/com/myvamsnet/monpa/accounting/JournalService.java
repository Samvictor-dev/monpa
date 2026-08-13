package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.myvamsnet.monpa.accounting.ReferenceGenerator.generateJournalReference;


@Service
@RequiredArgsConstructor
public class JournalService {

    private final PostingService postingService;

    public Journal createJournal(
            JournalType type,
            Money money,
            String narration
    ) {

        return Journal.create(
                type,
                money,
                narration,
                generateJournalReference()
        );

    }

    public LedgerEntry createDebitEntry(
            Journal journal,
            LedgerAccount account,
            Money money,
            String description
    ) {

        LedgerEntry entry =
                LedgerEntry.debit(
                        journal,
                        account,
                        money,
                        description
                );

        journal.addEntry(entry);

        return entry;
    }

    public LedgerEntry createCreditEntry(
            Journal journal,
            LedgerAccount account,
            Money money,
            String description
    ) {

        LedgerEntry entry =
                LedgerEntry.credit(
                        journal,
                        account,
                        money,
                        description
                );

        journal.addEntry(entry);

        return entry;
    }

    @Transactional
    public void validateAndPost(Journal journal) {

        postingService.post(journal);

    }

}
