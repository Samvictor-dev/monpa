package com.myvamsnet.monpa.common.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

    private static final int SCALE = 2;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    private Money(BigDecimal amount, Currency currency) {

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = amount.setScale(SCALE, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    // ---------------------------------------------------------
    // Factory methods
    // ---------------------------------------------------------


    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(
            String amount,
            Currency currency
    ) {

        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException(
                    "Amount cannot be empty."
            );
        }

        return new Money(
                new BigDecimal(amount),
                currency
        );
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public static Money zero(
            Currency currency
    ) {

        return new Money(
                BigDecimal.ZERO,
                currency
        );
    }


    // ---------------------------------------------------------
    // Arithmetic
    // ---------------------------------------------------------

    public Money add(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        return new Money(
                amount.add(other.amount),
                currency
        );
    }


    public Money subtract(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        BigDecimal result =
                amount.subtract(
                        other.amount
                );

        if (result.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        return new Money(
                result,
                currency
        );
    }


    // ---------------------------------------------------------
    // Comparisons
    // ---------------------------------------------------------

    public boolean isPositive() {

        return amount.compareTo(
                BigDecimal.ZERO
        ) > 0;
    }


    public boolean isZero() {

        return amount.compareTo(
                BigDecimal.ZERO
        ) == 0;
    }


    public boolean isGreaterThan(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        return amount.compareTo(
                other.amount
        ) > 0;
    }


    public boolean isGreaterThanOrEqualTo(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        return amount.compareTo(
                other.amount
        ) >= 0;
    }


    public boolean isLessThan(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        return amount.compareTo(
                other.amount
        ) < 0;
    }


    public boolean isLessThanOrEqualTo(
            Money other
    ) {

        validateOther(other);
        validateCurrency(other);

        return amount.compareTo(
                other.amount
        ) <= 0;
    }


    // ---------------------------------------------------------
    // Validation
    // ---------------------------------------------------------

    private void validateOther(
            Money other
    ) {

        if (other == null) {

            throw new IllegalArgumentException(
                    "Money cannot be null."
            );
        }
    }


    private void validateCurrency(
            Money other
    ) {

        if (currency != other.currency) {

            throw new IllegalArgumentException(
                    "Currencies must match."
            );
        }
    }


    // ---------------------------------------------------------
    // Equality
    // ---------------------------------------------------------

    @Override
    public boolean equals(
            Object o
    ) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Money money)) {
            return false;
        }

        return amount.compareTo(
                money.amount
        ) == 0
                && currency == money.currency;
    }


    @Override
    public int hashCode() {

        return Objects.hash(
                amount.stripTrailingZeros(),
                currency
        );
    }


    // ---------------------------------------------------------
    // String representation
    // ---------------------------------------------------------

    @Override
    public String toString() {

        return currency + " " + amount;
    }
}