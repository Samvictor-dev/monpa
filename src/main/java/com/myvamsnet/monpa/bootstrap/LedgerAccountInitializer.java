package com.myvamsnet.monpa.bootstrap;

import com.myvamsnet.monpa.model.LedgerAccount;
import com.myvamsnet.monpa.model.LedgerAccountCategory;
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

        createIfMissing(
                "1001",
                "Cash",
                LedgerAccountType.CASH,
                LedgerAccountCategory.ASSET
        );

        createIfMissing(
                "2001",
                "Customer Liability",
                LedgerAccountType.CUSTOMER_LIABILITY,
                LedgerAccountCategory.LIABILITY
        );

        createIfMissing(
                "3001",
                "Fee Revenue",
                LedgerAccountType.FEE_REVENUE,
                LedgerAccountCategory.REVENUE
        );

        createIfMissing(
                "4001",
                "Settlement",
                LedgerAccountType.SETTLEMENT,
                LedgerAccountCategory.ASSET
        );

        createIfMissing(
                "5001",
                "Suspense",
                LedgerAccountType.SUSPENSE,
                LedgerAccountCategory.ASSET
        );
    }


    private void createIfMissing(
            String accountNumber,
            String accountName,
            LedgerAccountType type,
            LedgerAccountCategory category
    ) {

        if (repository.existsByType(type)) {
            return;
        }

        LedgerAccount account =
                new LedgerAccount(
                        accountName,
                        accountNumber,
                        type,
                        category
                );

        repository.save(account);
    }
}
