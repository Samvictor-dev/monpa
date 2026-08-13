package com.myvamsnet.monpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ledger_accounts")
public class LedgerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountCategory category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", unique = true)
    private Wallet wallet;

    public LedgerAccount(
            String accountName,
            String accountNumber,
            LedgerAccountType type,
            LedgerAccountCategory category
    ) {

        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException(
                    "Account name cannot be empty."
            );
        }

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Account number cannot be empty."
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Account type cannot be null."
            );
        }

        if (category == null) {
            throw new IllegalArgumentException(
                    "Account category cannot be null."
            );
        }

        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.type = type;
        this.category = category;
    }

    public LedgerBalanceType getNormalBalance() {

        return switch (category) {

            case ASSET,
                 EXPENSE ->
                    LedgerBalanceType.DEBIT;

            case LIABILITY,
                 EQUITY,
                 REVENUE ->
                    LedgerBalanceType.CREDIT;
        };
    }



}