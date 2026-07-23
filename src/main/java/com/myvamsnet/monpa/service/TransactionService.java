package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.TransactionRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletRepository walletRepository;

    private final TransactionMapper transactionMapper;

    private final TransactionRepository transactionRepository;


    @Transactional
    public Transaction recordTransferOut(
            Wallet wallet,
            Money amount,
            String description,
            String reference) {

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());

        transaction.setTransferReference(reference);

        transaction.setWallet(wallet);

        transaction.setBalanceAfterTransaction(wallet.getBalance());

        transaction.setType(TransactionType.TRANSFER_OUT);

        transaction.setStatus(TransactionStatus.SUCCESS);

        transaction.setAmount(amount.getAmount());

        transaction.setCurrency(amount.getCurrency());

        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void recordTransferIn(
            Wallet wallet,
            Money amount,
            String description,
            String reference) {

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());

        transaction.setTransferReference(reference);

        transaction.setWallet(wallet);

        transaction.setBalanceAfterTransaction(wallet.getBalance());

        transaction.setType(TransactionType.TRANSFER_IN);

        transaction.setStatus(TransactionStatus.SUCCESS);

        transaction.setAmount(amount.getAmount());

        transaction.setCurrency(amount.getCurrency());

        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }

    public String generateTransactionReference() {
        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }

    public String generateTransferReference() {
        return "TRF-" +
                LocalDate.now()
                        .format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    @Transactional
    public Transaction recordDeposit(
            Wallet wallet,
            Money amount,
            String description,
            String reference
    ) {

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(generateTransactionReference());

        transaction.setTransferReference(reference);

        transaction.setWallet(wallet);

        transaction.setBalanceAfterTransaction(wallet.getBalance());

        transaction.setType(TransactionType.DEPOSIT);

        transaction.setStatus(TransactionStatus.SUCCESS);

        transaction.setAmount(amount.getAmount());

        transaction.setCurrency(amount.getCurrency());

        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    private String generateReference() {

        return "MM-" +
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,8)
                        .toUpperCase();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionHistory(Long userId) {

        Wallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new WalletNotFoundException(userId));

        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(
                        wallet.getId()
                )
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

    }
}
