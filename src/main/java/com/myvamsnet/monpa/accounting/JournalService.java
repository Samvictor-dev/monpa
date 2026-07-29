package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.accounting.validator.JournalValidator;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.JournalRepository;
import com.myvamsnet.monpa.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    private final PostingService postingService;
    private final JournalValidator journalValidator;

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

        return createEntry(
                journal,
                account,
                LedgerEntryType.DEBIT,
                money,
                description
        );

    }

    public LedgerEntry createCreditEntry(
            Journal journal,
            LedgerAccount account,
            Money money,
            String description
    ) {

        return createEntry(
                journal,
                account,
                LedgerEntryType.CREDIT,
                money,
                description
        );

    }

    private LedgerEntry createEntry(
            Journal journal,
            LedgerAccount account,
            LedgerEntryType entryType,
            Money money,
            String description
    ) {

        LedgerEntry entry = new LedgerEntry();

        entry.setJournal(journal);
        entry.setLedgerAccount(account);
        entry.setEntryType(entryType);
        entry.setMoney(money);
        entry.setDescription(description);

        String suffix = switch (entryType) {
            case DEBIT -> "-DR";
            case CREDIT -> "-CR";
        };

        entry.setReference(
                journal.getJournalReference() + suffix
        );

        entry.setCreatedAt(now);

        return ledgerEntryRepository.save(entry);
    }

    @Transactional
    public void validateAndPost(Journal journal) {

        journalValidator.validate(journal);

        postingService.post(journal);

    }

}
