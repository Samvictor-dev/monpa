package com.myvamsnet.monpa.model;

import com.myvamsnet.monpa.common.exception.InvalidTransactionStateException;
import com.myvamsnet.monpa.common.valueobject.Currency;
import com.myvamsnet.monpa.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_reference", nullable = false, unique = true)
    private String transactionReference;

    @Column(name = "transfer_reference")
    private String transferReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus status;

    @Column(name = "balance_after_transaction", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfterTransaction;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reversal_transaction_id")
    private Transaction reversalTransaction;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // getters & setters


//    private String initiatedBy;
//    private String channel;



    public void reverse(Transaction reversal) {

        if (status == TransactionStatus.REVERSED) {
            throw new InvalidTransactionStateException(
                    "Transaction has already been reversed."
            );
        }

        this.status = TransactionStatus.REVERSED;
        this.reversalTransaction = reversal;
    }

    public void linkToOriginal(Transaction original) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "Original transaction cannot be null."
            );
        }

        this.reversalTransaction = original;
    }

    //Static Factory (create domain factory)
    public static Transaction deposit(

            Wallet wallet,

            Money money,

            String description,

            String transactionReference

    ) {

        if (wallet == null) {
            throw new IllegalArgumentException(
                    "Wallet cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (transactionReference == null || transactionReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Transaction reference cannot be empty."
            );
        }

        Transaction transaction = new Transaction();

        transaction.wallet = wallet;

        transaction.amount = money.getAmount();

        transaction.currency = money.getCurrency();

        transaction.type = TransactionType.DEPOSIT;

        transaction.status = TransactionStatus.SUCCESS;

        transaction.transactionReference = transactionReference;

        transaction.description = description;

        transaction.balanceAfterTransaction =
                wallet.getBalance();

        return transaction;

    }

    public static Transaction withdrawal(

            Wallet wallet,

            Money money,

            String description,

            String transactionReference

    ) {

        if (wallet == null) {
            throw new IllegalArgumentException(
                    "Wallet cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (transactionReference == null
                || transactionReference.isBlank()) {

            throw new IllegalArgumentException(
                    "Transaction reference cannot be empty."
            );
        }

        Transaction transaction = new Transaction();

        transaction.wallet = wallet;

        transaction.amount = money.getAmount();

        transaction.currency = money.getCurrency();

        transaction.type = TransactionType.WITHDRAWAL;

        transaction.status = TransactionStatus.SUCCESS;

        transaction.transactionReference =
                transactionReference;

        transaction.description =
                description;

        transaction.balanceAfterTransaction =
                wallet.getBalance();

        return transaction;
    }

    public static Transaction transferOut(

            Wallet wallet,

            Money money,

            String description,

            String transactionReference,

            String transferReference

    ) {

        if (wallet == null) {
            throw new IllegalArgumentException(
                    "Wallet cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (transferReference == null
                || transferReference.isBlank()) {

            throw new IllegalArgumentException(
                    "Transfer reference cannot be empty."
            );
        }

        Transaction transaction = new Transaction();

        transaction.wallet = wallet;

        transaction.amount = money.getAmount();

        transaction.currency = money.getCurrency();

        transaction.type =
                TransactionType.TRANSFER_OUT;

        transaction.status =
                TransactionStatus.SUCCESS;

        transaction.transferReference =
                transferReference;

        transaction.description =
                description;

        transaction.balanceAfterTransaction =
                wallet.getBalance();

        transaction.transactionReference =
                transactionReference;

        return transaction;
    }

    public static Transaction transferIn(

            Wallet wallet,

            Money money,

            String description,

            String transactionReference,

            String transferReference

    ) {

        if (wallet == null) {
            throw new IllegalArgumentException(
                    "Wallet cannot be null."
            );
        }

        if (money == null) {
            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }

        if (transferReference == null
                || transferReference.isBlank()) {

            throw new IllegalArgumentException(
                    "Transfer reference cannot be empty."
            );
        }

        Transaction transaction = new Transaction();

        transaction.wallet = wallet;

        transaction.amount = money.getAmount();

        transaction.currency = money.getCurrency();

        transaction.type =
                TransactionType.TRANSFER_IN;

        transaction.status =
                TransactionStatus.SUCCESS;

        transaction.transferReference =
                transferReference;

        transaction.description =
                description;

        transaction.balanceAfterTransaction =
                wallet.getBalance();

        transaction.transactionReference =
                transactionReference;

        return transaction;
    }

    public static Transaction reversal(

            Transaction original,

            Wallet wallet,

            String description,

            String transactionReference

    ) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "Original transaction cannot be null."
            );
        }

        if (wallet == null) {
            throw new IllegalArgumentException(
                    "Wallet cannot be null."
            );
        }

        if (transactionReference == null
                || transactionReference.isBlank()) {

            throw new IllegalArgumentException(
                    "Transaction reference cannot be empty."
            );
        }

        Transaction reversal = new Transaction();

        reversal.wallet = wallet;

        reversal.amount = original.amount;

        reversal.currency = original.currency;

        reversal.type = original.type;

        reversal.status = TransactionStatus.SUCCESS;

        reversal.transactionReference =
                transactionReference;

        reversal.transferReference =
                original.transferReference;

        reversal.description =
                description;

        reversal.balanceAfterTransaction =
                wallet.getBalance();

        return reversal;
    }

    //Removing Public Setters
    public void attachJournal(

            Journal journal

    ) {

        if (journal == null) {

            throw new IllegalArgumentException(
                    "Journal cannot be null."
            );

        }

        this.journal = journal;

    }

    public void markFailed() {

        this.status = TransactionStatus.FAILED;

    }

    public void markSuccessful() {

        this.status = TransactionStatus.SUCCESS;

    }

}