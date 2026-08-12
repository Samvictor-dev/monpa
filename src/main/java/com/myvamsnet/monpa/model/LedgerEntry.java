package com.myvamsnet.monpa.model;

import com.myvamsnet.monpa.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_account_id")
    private LedgerAccount ledgerAccount;

    @Enumerated(EnumType.STRING)
    private LedgerEntryType entryType;

    @Embedded
    private Money money;

    @Column(nullable = false)
    private String description;

    @Column(nullable =false)
    private String reference;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static LedgerEntry debit(
            Journal journal,
            LedgerAccount ledgerAccount,
            Money money,
            String description
    ) {

        if (journal == null) {
            throw new IllegalArgumentException(
                    "Journal cannot be null."
            );
        }

        if (ledgerAccount == null) {
            throw new IllegalArgumentException(
                    "Ledger account cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (!money.isPositive()) {
            throw new IllegalArgumentException(
                    "Ledger amount must be greater than zero."
            );
        }

        LedgerEntry entry = new LedgerEntry();

        entry.journal = journal;

        entry.ledgerAccount = ledgerAccount;

        entry.entryType = LedgerEntryType.DEBIT;

        entry.money = money;

        entry.description = description;

        entry.reference =
                journal.getJournalReference() + "-DR";

        entry.createdAt =
                LocalDateTime.now();

        return entry;
    }

    public static LedgerEntry credit(
            Journal journal,
            LedgerAccount ledgerAccount,
            Money money,
            String description
    ) {

        if (journal == null) {
            throw new IllegalArgumentException(
                    "Journal cannot be null."
            );
        }

        if (ledgerAccount == null) {
            throw new IllegalArgumentException(
                    "Ledger account cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (!money.isPositive()) {
            throw new IllegalArgumentException(
                    "Ledger amount must be greater than zero."
            );
        }

        LedgerEntry entry = new LedgerEntry();

        entry.journal = journal;

        entry.ledgerAccount = ledgerAccount;

        entry.entryType = LedgerEntryType.CREDIT;

        entry.money = money;

        entry.description = description;

        entry.reference =
                journal.getJournalReference() + "-CR";

        entry.createdAt =
                LocalDateTime.now();

        return entry;
    }
}
