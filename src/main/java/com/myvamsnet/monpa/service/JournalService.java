package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.common.exception.LedgerAccountNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.JournalRepository;
import com.myvamsnet.monpa.repository.LedgerAccountRepository;
import com.myvamsnet.monpa.repository.LedgerEntryRepository;
import com.myvamsnet.monpa.util.ReferenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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

        entry.setReference(
                journal.getJournalReference() + "-DR"
        );

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

        entry.setReference(
                journal.getJournalReference() + "-CR"
        );

        entry.setCreatedAt(LocalDateTime.now());

        return ledgerEntryRepository.save(entry);

    }

    public void postJournal(Journal journal) {

        journal.setStatus(JournalStatus.POSTED);

        journal.setPostedAt(LocalDateTime.now());

        journalRepository.save(journal);

    }

    @Transactional
    public Journal recordDeposit(
            Wallet wallet,
            Money money,
            String narration
    ) {

        Journal journal = createJournal(
                JournalType.DEPOSIT,
                money,
                narration
        );

        LedgerAccount cashAccount =
                ledgerAccountRepository
                        .findByType(LedgerAccountType.CASH)
                        .orElseThrow(() ->
                                new LedgerAccountNotFoundException(
                                        LedgerAccountType.CASH
                                ));

        LedgerAccount customerLiability =
                ledgerAccountRepository
                        .findByType(LedgerAccountType.CUSTOMER_LIABILITY)
                        .orElseThrow(() ->
                                new LedgerAccountNotFoundException(
                                        LedgerAccountType.CUSTOMER_LIABILITY
                                ));

        createDebitEntry(
                journal,
                cashAccount,
                money,
                "Cash received"
        );

        createCreditEntry(
                journal,
                customerLiability,
                money,
                "Customer wallet funded"
        );

        validateJournal(journal);

        postJournal(journal);

        return journal;

    }

    @Transactional
    public Journal recordWithdrawal(
            Wallet wallet,
            Money money,
            String narration
    ) {

        Journal journal = createJournal(
                JournalType.WITHDRAWAL,
                money,
                narration
        );

        LedgerAccount customerLiability =
                ledgerAccountRepository
                        .findByType(
                                LedgerAccountType.CUSTOMER_LIABILITY
                        )
                        .orElseThrow(() ->
                                new LedgerAccountNotFoundException(
                                        LedgerAccountType.CUSTOMER_LIABILITY
                                ));

        LedgerAccount cash =
                ledgerAccountRepository
                        .findByType(
                                LedgerAccountType.CASH
                        )
                        .orElseThrow(() ->
                                new LedgerAccountNotFoundException(
                                        LedgerAccountType.CASH
                                ));

        createDebitEntry(
                journal,
                customerLiability,
                money,
                "Customer withdrawal"
        );

        createCreditEntry(
                journal,
                cash,
                money,
                "Cash paid out"
        );

        validateJournal(journal);

        postJournal(journal);

        return journal;

    }

    private void validateJournal(
            Journal journal
    ) {

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
