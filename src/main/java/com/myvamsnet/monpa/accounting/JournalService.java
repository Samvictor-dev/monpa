package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    private final PostingService postingService;

    LocalDateTime now = LocalDateTime.now();


    public Journal createJournal(
            JournalType type,
            Money money,
            String narration
    ) {

        Journal journal = new Journal();

        journal.setJournalReference(
                ReferenceGenerator.generateJournalReference()
        );

        journal.setType(type);

        journal.setStatus(JournalStatus.PENDING);

        journal.setAmount(money.getAmount());

        journal.setCurrency(money.getCurrency().name());

        journal.setNarration(narration);

        journal.setCreatedAt(now);

        return journalRepository.save(journal);

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
