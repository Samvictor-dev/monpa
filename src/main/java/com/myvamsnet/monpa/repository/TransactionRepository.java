package com.myvamsnet.monpa.repository;


import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.TransactionType;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);

    Optional<Transaction> findByTransactionReference(String transactionReference);

    Page<Transaction> findByWalletOrderByCreatedAtDesc(
            Wallet wallet,
            Pageable pageable
    );

    Page<Transaction> findByWalletAndTypeOrderByCreatedAtDesc(
            Wallet wallet,
            TransactionType type,
            Pageable pageable
    );

    Optional<Transaction> findByTransferReferenceAndType(
            String transferReference,
            TransactionType type
    );

}
