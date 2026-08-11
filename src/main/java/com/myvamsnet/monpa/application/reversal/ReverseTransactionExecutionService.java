package com.myvamsnet.monpa.application.reversal;

import com.myvamsnet.monpa.accounting.ReferenceGenerator;
import com.myvamsnet.monpa.accounting.ReversalService;
import com.myvamsnet.monpa.common.exception.InvalidTransactionStateException;
import com.myvamsnet.monpa.common.exception.TransactionNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.TransactionRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReverseTransactionExecutionService {

    private final TransactionRepository transactionRepository;

    private final WalletRepository walletRepository;

    private final ReversalService reversalService;

    private final TransactionMapper transactionMapper;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class
    )
    public TransactionResponse execute(
            String transactionReference
    ) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionReference(transactionReference)
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        transactionReference
                                ));

        if (transaction.getStatus() != TransactionStatus.SUCCESS) {

            throw new InvalidTransactionStateException(
                    "Only successful transactions can be reversed."
            );

        }

        Journal reversalJournal =
                reversalService.reverse(
                        transaction.getJournal(),
                        "Reversal of "
                                + transaction.getTransactionReference()
                );

        switch (transaction.getType()) {

            case DEPOSIT -> reverseDeposit(
                    transaction,
                    reversalJournal
            );

            case WITHDRAWAL -> reverseWithdrawal(
                    transaction,
                    reversalJournal
            );

            case TRANSFER_OUT ->
                    reverseTransferOut(
                            transaction,
                            reversalJournal
                    );

            case TRANSFER_IN ->
                    throw new InvalidTransactionStateException(
                            "Reverse the outgoing transfer transaction instead."
                    );

            default -> throw new InvalidTransactionStateException(
                    "Unsupported transaction type."
            );

        }

        transactionRepository.save(transaction);

        return transactionMapper.toResponse(
                transaction.getReversalTransaction()
        );

    }

    private void reverseDeposit(
            Transaction transaction,
            Journal reversalJournal
    ) {

        Wallet wallet = transaction.getWallet();

        Money money = Money.of(
                transaction.getAmount(),
                wallet.getCurrency()
        );

        wallet.withdraw(money);

        walletRepository.save(wallet);

        createReversalTransaction(
                transaction,
                reversalJournal,
                wallet
        );

    }

    private void reverseWithdrawal(
            Transaction transaction,
            Journal reversalJournal
    ) {

        Wallet wallet = transaction.getWallet();

        Money money = Money.of(
                transaction.getAmount(),
                wallet.getCurrency()
        );

        wallet.deposit(money);

        walletRepository.save(wallet);

        createReversalTransaction(
                transaction,
                reversalJournal,
                wallet
        );

    }

    private void reverseTransferOut(
            Transaction outgoingTransaction,
            Journal reversalJournal
    ) {

        Transaction incomingTransaction =
                transactionRepository
                        .findByTransferReferenceAndType(
                                outgoingTransaction.getTransferReference(),
                                TransactionType.TRANSFER_IN
                        )
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        "Matching incoming transfer not found."
                                ));

        if (incomingTransaction.getStatus() != TransactionStatus.SUCCESS) {

            throw new InvalidTransactionStateException(
                    "Unsupported or Transfer has already been reversed."
            );

        }

        Wallet senderWallet =
                outgoingTransaction.getWallet();

        Wallet receiverWallet =
                incomingTransaction.getWallet();

        Money money = Money.of(
                outgoingTransaction.getAmount(),
                senderWallet.getCurrency()
        );

        // Refund sender
        senderWallet.deposit(money);

        // Remove money from receiver
        receiverWallet.withdraw(money);

        walletRepository.save(senderWallet);

        walletRepository.save(receiverWallet);

        createReversalTransaction(
                outgoingTransaction,
                reversalJournal,
                senderWallet
        );

        createReversalTransaction(
                incomingTransaction,
                reversalJournal,
                receiverWallet
        );

    }

    private void createReversalTransaction(
            Transaction transaction,
            Journal reversalJournal,
            Wallet wallet
    ) {

        Transaction reversal = new Transaction();

        reversal.setTransactionReference(
                ReferenceGenerator.generateTransactionReference()
        );

        reversal.setTransferReference(
                transaction.getTransferReference()
        );

        reversal.setType(
                transaction.getType()
        );

        reversal.setAmount(
                transaction.getAmount()
        );

        reversal.setCurrency(
                transaction.getCurrency()
        );

        reversal.setStatus(
                TransactionStatus.SUCCESS
        );

        reversal.setWallet(
                wallet
        );

        reversal.setJournal(
                reversalJournal
        );

        reversal.setDescription(
                "Reversal of "
                        + transaction.getDescription()
        );

        reversal.setBalanceAfterTransaction(
                wallet.getBalance()
        );

        transaction.reverse(reversal);

        reversal.linkToOriginal(transaction);

        transactionRepository.save(
                reversal
        );

    }


}
