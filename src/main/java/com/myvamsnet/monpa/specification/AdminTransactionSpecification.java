package com.myvamsnet.monpa.specification;

import com.myvamsnet.monpa.dto.transaction.AdminTransactionFilter;
import com.myvamsnet.monpa.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import static com.myvamsnet.monpa.specification.TransactionSpecification.*;

public final class AdminTransactionSpecification {

    private AdminTransactionSpecification() {
    }

    public static Specification<Transaction> build(
            AdminTransactionFilter filter
    ) {

        return Specification
                .where(hasType(filter.getType()))
                .and(hasStatus(filter.getStatus()))
                .and(hasTransactionReference(filter.getTransactionReference()))
                .and(hasTransferReference(filter.getTransferReference()))
                .and(createdBetween(
                        filter.getFrom(),
                        filter.getTo()
                ));

    }

}