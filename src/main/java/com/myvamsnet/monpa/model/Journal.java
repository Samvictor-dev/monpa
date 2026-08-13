package com.myvamsnet.monpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, unique = true)
    private String journalReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalStatus status;

    @Column(nullable = false)
    private String narration;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime postedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reversed_journal_id")
    private Journal reversedJournal;

    @OneToMany(
            mappedBy = "journal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LedgerEntry> entries = new ArrayList<>();


    public void addEntry(LedgerEntry entry) {

        if (entry == null) {

            throw new IllegalArgumentException(
                    "Ledger entry cannot be null."
            );
        }

        ensurePending();

        entries.add(entry);
    }

    public List<LedgerEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }


    public void post() {

        if (status != JournalStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending journals can be posted."
            );
        }

        this.status = JournalStatus.POSTED;

        this.postedAt = LocalDateTime.now();
    }

    public boolean isPosted() {

        return status == JournalStatus.POSTED;

    }

    public boolean isPending() {

        return status == JournalStatus.PENDING;

    }

    public void ensurePending() {

        if (status != JournalStatus.PENDING) {

            throw new IllegalStateException(
                    "Posted journal cannot be modified."
            );
        }
    }

    public void reverseOf(Journal originalJournal) {

        if (originalJournal == null) {

            throw new IllegalArgumentException(
                    "Original journal cannot be null."
            );
        }

        if (originalJournal == this) {

            throw new IllegalArgumentException(
                    "A journal cannot reverse itself."
            );
        }

        if (!originalJournal.isPosted()) {

            throw new IllegalStateException(
                    "Only posted journals can be reversed."
            );
        }

        this.reversedJournal = originalJournal;

        this.type = JournalType.REVERSAL;
    }

}