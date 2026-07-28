package com.myvamsnet.monpa.bootstrap;

import com.myvamsnet.monpa.model.LedgerAccount;
import com.myvamsnet.monpa.model.LedgerAccountType;
import com.myvamsnet.monpa.repository.LedgerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerAccountInitializer
        implements CommandLineRunner {

    private final LedgerAccountRepository repository;

    @Override
    public void run(String... args) {

        if (!repository.existsByType(LedgerAccountType.CASH)) {

            LedgerAccount cash = new LedgerAccount();

            cash.setAccountNumber("1001");

            cash.setAccountName("Cash");

            cash.setType(LedgerAccountType.CASH);

            repository.save(cash);

        }

        if (!repository.existsByType(
                LedgerAccountType.CUSTOMER_LIABILITY)) {

            LedgerAccount account = new LedgerAccount();

            account.setAccountNumber("2001");

            account.setAccountName(
                    "Customer Liability"
            );

            account.setType(
                    LedgerAccountType.CUSTOMER_LIABILITY
            );

            repository.save(account);

        }

        if (!repository.existsByType(
                LedgerAccountType.FEE_REVENUE)) {

            LedgerAccount account = new LedgerAccount();

            account.setAccountNumber("3001");

            account.setAccountName("Fee Revenue");

            account.setType(
                    LedgerAccountType.FEE_REVENUE
            );

            repository.save(account);

        }

        if (!repository.existsByType(
                LedgerAccountType.SETTLEMENT)) {

            LedgerAccount account = new LedgerAccount();

            account.setAccountNumber("4001");

            account.setAccountName("Settlement");

            account.setType(
                    LedgerAccountType.SETTLEMENT
            );

            repository.save(account);

        }

        if (!repository.existsByType(
                LedgerAccountType.SUSPENSE)) {

            LedgerAccount account = new LedgerAccount();

            account.setAccountNumber("5001");

            account.setAccountName("Suspense");

            account.setType(
                    LedgerAccountType.SUSPENSE
            );

            repository.save(account);

        }

    }

}
