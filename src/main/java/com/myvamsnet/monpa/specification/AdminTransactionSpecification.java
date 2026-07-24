package com.myvamsnet.monpa.specification;

import com.myvamsnet.monpa.dto.transaction.AdminTransactionFilter;
import com.myvamsnet.monpa.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import static com.myvamsnet.monpa.specification.TransactionSpecification.*;

import jakarta.persistence.criteria.Join;

import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import java.math.BigDecimal;


public final class AdminTransactionSpecification {

    private AdminTransactionSpecification() {
    }

    public static Specification<Transaction> build(
            AdminTransactionFilter filter
    ) {

        return Specification

                .where(hasType(filter.getType()))

                .and(hasStatus(filter.getStatus()))

                .and(hasTransactionReference(
                        filter.getTransactionReference()
                ))

                .and(hasTransferReference(
                        filter.getTransferReference()
                ))

                .and(hasEmail(
                        filter.getEmail()
                ))

                .and(hasWalletNumber(
                        filter.getWalletNumber()
                ))

                .and(amountBetween(
                        filter.getMinimumAmount(),
                        filter.getMaximumAmount()
                ))

                .and(createdBetween(
                        filter.getFrom(),
                        filter.getTo()
                ));

    }

    public static Specification<Transaction> hasTransferReference(
            String reference
    ) {

        return (root, query, cb) ->

                reference == null || reference.isBlank()

                        ? cb.conjunction()

                        : cb.equal(
                        root.get("transferReference"),
                        reference
                );

    }

    public static Specification<Transaction> hasEmail(String email) {

        return (root, query, cb) -> {

            if (email == null || email.isBlank()) {
                return cb.conjunction();
            }

            Join<Transaction, Wallet> wallet =
                    root.join("wallet");

            Join<Wallet, User> user =
                    wallet.join("user");

            return cb.equal(
                    cb.lower(user.get("email")),
                    email.toLowerCase()
            );

        };

    }

    public static Specification<Transaction> hasWalletNumber(
            String walletNumber
    ) {

        return (root, query, cb) -> {

            if (walletNumber == null || walletNumber.isBlank()) {
                return cb.conjunction();
            }

            Join<Transaction, Wallet> wallet =
                    root.join("wallet");

            return cb.equal(
                    wallet.get("walletNumber"),
                    walletNumber
            );

        };

    }

    public static Specification<Transaction> amountBetween(

            BigDecimal minimum,

            BigDecimal maximum

    ) {

        return (root, query, cb) -> {

            if (minimum == null && maximum == null) {
                return cb.conjunction();
            }

            if (minimum != null && maximum != null) {

                return cb.between(
                        root.get("amount"),
                        minimum,
                        maximum
                );

            }

            if (minimum != null) {

                return cb.greaterThanOrEqualTo(
                        root.get("amount"),
                        minimum
                );

            }

            return cb.lessThanOrEqualTo(
                    root.get("amount"),
                    maximum
            );

        };

    }

}