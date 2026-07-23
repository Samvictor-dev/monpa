package com.myvamsnet.monpa.repository;


import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);

    Optional<Transaction> findByTransactionReference(String transactionReference);

    Page<Transaction> findByWalletOrderByCreatedAtDesc(
            Wallet wallet,
            Pageable pageable
    );

}
