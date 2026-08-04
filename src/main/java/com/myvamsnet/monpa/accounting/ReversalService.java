package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.accounting.validator.JournalValidator;
import com.myvamsnet.monpa.common.exception.InvalidTransactionStateException;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.JournalRepository;
import com.myvamsnet.monpa.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReversalService {

    private final JournalRepository journalRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final PostingService postingService;
    private final JournalValidator journalValidator;

    @Transactional
    public Journal reverse(
            Journal originalJournal,
            String narration
    ) {

        // Prevent duplicate reversal
        if (journalRepository.existsByReversedJournal(originalJournal)) {
            throw new InvalidTransactionStateException(
                    "Journal has already been reversed."
            );
        }

        LocalDateTime now = LocalDateTime.now();

        // Create reversal journal
        Journal reversalJournal = new Journal();

        reversalJournal.setJournalReference(
                ReferenceGenerator.generateJournalReference()
        );

        reversalJournal.setType(
                JournalType.REVERSAL
        );

        reversalJournal.setStatus(
                JournalStatus.PENDING
        );

        reversalJournal.setNarration(
                narration
        );

        reversalJournal.setAmount(
                originalJournal.getAmount()
        );

        reversalJournal.setCurrency(
                originalJournal.getCurrency()
        );

        reversalJournal.setCreatedAt(now);

        reversalJournal.setReversedJournal(
                originalJournal
        );

        journalRepository.save(reversalJournal);

        // Load original entries
        List<LedgerEntry> originalEntries =
                ledgerEntryRepository.findByJournal(originalJournal);

        if (originalEntries.isEmpty()) {
            throw new IllegalStateException(
                    "Journal contains no ledger entries."
            );
        }

        // Create reversal entries
        for (LedgerEntry originalEntry : originalEntries) {

            LedgerEntry reversalEntry =
                    createReversalEntry(
                            originalEntry,
                            reversalJournal,
                            now
                    );

            ledgerEntryRepository.save(reversalEntry);
        }

        // Validate journal
        journalValidator.validate(reversalJournal);

        // Post journal
        postingService.post(reversalJournal);

        return reversalJournal;
    }

    private LedgerEntry createReversalEntry(

            LedgerEntry originalEntry,
            Journal reversalJournal,
            LocalDateTime now

    ) {

        LedgerEntry reversalEntry =
                new LedgerEntry();

        reversalEntry.setJournal(
                reversalJournal
        );

        reversalEntry.setLedgerAccount(
                originalEntry.getLedgerAccount()
        );

        // Money is immutable, safe to reuse
        reversalEntry.setMoney(
                originalEntry.getMoney()
        );

        reversalEntry.setDescription(
                "Reversal of " +
                        originalEntry.getDescription()
        );

        reversalEntry.setReference(
                reversalJournal.getJournalReference()
        );

        reversalEntry.setCreatedAt(now);

        reversalEntry.setEntryType(

                originalEntry.getEntryType() ==
                        LedgerEntryType.DEBIT

                        ? LedgerEntryType.CREDIT
                        : LedgerEntryType.DEBIT

        );

        return reversalEntry;
    }

}
