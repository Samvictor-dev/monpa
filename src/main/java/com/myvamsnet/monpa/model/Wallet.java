package com.myvamsnet.monpa.model;

import com.myvamsnet.monpa.common.valueobject.Currency;
import com.myvamsnet.monpa.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency = Currency.NGN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status = WalletStatus.ACTIVE;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(
            mappedBy = "wallet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transaction> transactions = new ArrayList<>();

    @Version
    private Long version;

    // Getters

    private Wallet(
            User user,
            String accountNumber,
            Money balance
    ) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = balance.getAmount();
    }

    public static Wallet create(User user, String accountNumber) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return new Wallet(
                user,
                accountNumber,
                Money.zero(Currency.NGN)
        );
    }

    public static Wallet createFor(User user, String accountNumber) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Wallet wallet = new Wallet();

        wallet.user = user;
        wallet.accountNumber = accountNumber;
        wallet.balance = BigDecimal.ZERO;
        wallet.currency = Currency.NGN;
        wallet.status = WalletStatus.ACTIVE;

        return wallet;
    }

    private void ensureActive() {

        if (status != WalletStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Wallet is not active."
            );
        }
    }

    public void deposit(Money amount){

        validateCurrency(amount);

        ensureActive();

        if (!amount.isPositive()) {

            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero."
            );
        }

        balance = balance.add(amount.getAmount());

    }

    public void withdraw(Money amount){

        validateCurrency(amount);

        ensureActive();

        if(balance.compareTo(amount.getAmount()) < 0){

            throw new IllegalStateException(
                    "Insufficient balance."
            );

        }

        balance = balance.subtract(
                amount.getAmount()
        );

    }

    public void freeze(){

        if(status == WalletStatus.FROZEN){

            throw new IllegalStateException(
                    "Wallet already frozen."
            );

        }

        status = WalletStatus.FROZEN;

    }

    public void unfreeze(){

        if(status == WalletStatus.ACTIVE){

            throw new IllegalStateException(
                    "Wallet already active."
            );

        }

        status = WalletStatus.ACTIVE;

    }

    public boolean canWithdraw(
            Money amount
    ){

        validateCurrency(amount);

        return balance.compareTo(
                amount.getAmount()
        ) >= 0;

    }

    private void validateCurrency(
            Money amount
    ){

        if(currency != amount.getCurrency()){

            throw new IllegalArgumentException(
                    "Currency mismatch."
            );

        }

    }

    public boolean hasSufficientBalance(
            Money amount) {

        return balance.compareTo(amount.getAmount()) >= 0;
    }
}