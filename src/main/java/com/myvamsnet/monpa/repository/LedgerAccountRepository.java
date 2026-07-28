package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.LedgerAccount;
import com.myvamsnet.monpa.model.LedgerAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerAccountRepository
        extends JpaRepository<LedgerAccount, Long> {

    Optional<LedgerAccount> findByType(LedgerAccountType type);

    Optional<LedgerAccount> findByWalletId(Long walletId);

    boolean existsByType(LedgerAccountType type);

}
