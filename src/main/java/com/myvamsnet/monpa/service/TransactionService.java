package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.common.pagination.PageRequestFactory;
import com.myvamsnet.monpa.common.pagination.PaginationUtil;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.TransactionFilter;
import com.myvamsnet.monpa.dto.transaction.TransactionHistoryResponse;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.TransactionRepository;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;

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

//    @Transactional(readOnly = true)
//    public List<TransactionResponse> getTransactionHistory(Long userId) {
//
//        Wallet wallet = walletRepository
//                .findByUserId(userId)
//                .orElseThrow(() ->
//                        new WalletNotFoundException(userId));
//
//        return transactionRepository
//                .findByWalletIdOrderByCreatedAtDesc(
//                        wallet.getId()
//                )
//                .stream()
//                .map(transactionMapper::toResponse)
//                .toList();
//
//    }

    public PagedResponse<TransactionHistoryResponse> getTransactionHistory(
            String email,
            TransactionFilter filter,
            int page,
            int size
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        Pageable pageable =
                PageRequestFactory.defaultPage(page, size);

        Specification<Transaction> specification =
                Specification
                        .where(TransactionSpecification.hasWallet(wallet))
                        .and(TransactionSpecification.hasType(filter.getType()))
                        .and(TransactionSpecification.hasStatus(filter.getStatus()))
                        .and(TransactionSpecification.hasTransactionReference(filter.getReference()))
                        .and(TransactionSpecification.createdBetween(
                                filter.getFrom(),
                                filter.getTo()
                        ));

        Page<Transaction> transactionPage =
                transactionRepository.findAll(
                        specification,
                        pageable
                );

        Page<TransactionHistoryResponse> responsePage =
                transactionPage.map(transactionMapper::toHistoryResponse);

        return PaginationUtil.from(responsePage);
    }
}
