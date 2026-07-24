package com.myvamsnet.monpa.specification;

import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> hasWallet(Wallet wallet) {

        return (root, query, cb) ->
                cb.equal(root.get("wallet"), wallet);

    }

    public static Specification<Transaction> hasType(TransactionType type) {

        return (root, query, cb) ->

                type == null
                        ? cb.conjunction()
                        : cb.equal(root.get("type"), type);

    }

    public static Specification<Transaction> hasStatus(TransactionStatus status) {

        return (root, query, cb) ->

                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);

    }

    public static Specification<Transaction> hasTransactionReference(String reference) {

        return (root, query, cb) ->

                (reference == null || reference.isBlank())
                        ? cb.conjunction()
                        : cb.equal(root.get("transactionReference"), reference);

    }

    public static Specification<Transaction> hasTransferReference(String reference) {

        return (root, query, cb) ->

                (reference == null || reference.isBlank())
                        ? cb.conjunction()
                        : cb.equal(root.get("transferReference"), reference);

    }

    public static Specification<Transaction> createdBetween(
            LocalDateTime from,
            LocalDateTime to
    ) {

        return (root, query, cb) -> {

            if (from == null && to == null) {
                return cb.conjunction();
            }

            if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            }

            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            }

            return cb.lessThanOrEqualTo(root.get("createdAt"), to);

        };

    }

}
