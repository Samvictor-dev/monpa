package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.JournalRepository;
import com.myvamsnet.monpa.repository.LedgerAccountRepository;
import com.myvamsnet.monpa.repository.LedgerEntryRepository;
import com.myvamsnet.monpa.util.ReferenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerAccountRepository ledgerAccountRepository;


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

        journal.setCreatedAt(LocalDateTime.now());

        return journalRepository.save(journal);

    }

    public LedgerEntry createDebitEntry(
            Journal journal,
            LedgerAccount account,
            Money money,
            String description
    ) {

        LedgerEntry entry = new LedgerEntry();

        entry.setJournal(journal);

        entry.setLedgerAccount(account);

        entry.setEntryType(LedgerEntryType.DEBIT);

        entry.setMoney(money);

        entry.setDescription(description);

        entry.setCreatedAt(LocalDateTime.now());

        return ledgerEntryRepository.save(entry);

    }

    public LedgerEntry createCreditEntry(
            Journal journal,
            LedgerAccount account,
            Money money,
            String description
    ) {

        LedgerEntry entry = new LedgerEntry();

        entry.setJournal(journal);

        entry.setLedgerAccount(account);

        entry.setEntryType(LedgerEntryType.CREDIT);

        entry.setMoney(money);

        entry.setDescription(description);

        entry.setCreatedAt(LocalDateTime.now());

        return ledgerEntryRepository.save(entry);

    }

    public void postJournal(Journal journal) {

        journal.setStatus(JournalStatus.POSTED);

        journal.setPostedAt(LocalDateTime.now());

        journalRepository.save(journal);

    }

}
