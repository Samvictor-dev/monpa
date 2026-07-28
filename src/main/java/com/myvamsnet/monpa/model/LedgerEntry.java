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
}
