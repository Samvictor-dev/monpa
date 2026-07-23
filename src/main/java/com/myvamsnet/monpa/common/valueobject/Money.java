package com.myvamsnet.monpa.common.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;

    private final BigDecimal amount;
    private final Currency currency;

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

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money other) {

        validateCurrency(other);

        return new Money(
                amount.add(other.amount),
                currency
        );

    }

    private void validateCurrency(Money other){

        if(!currency.equals(other.currency)){

            throw new IllegalArgumentException(
                    "Currencies must match."
            );

        }

    }

    public Money subtract(Money other) {

        validateCurrency(other);

        BigDecimal result =
                amount.subtract(other.amount);

        if(result.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        return new Money(result, currency);

    }

    public boolean isPositive() {

        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isZero() {

        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isGreaterThan(Money other){

        validateCurrency(other);

        return amount.compareTo(other.amount) > 0;

    }

    public static Money zero(Currency currency){

        return new Money(
                BigDecimal.ZERO,
                currency
        );

    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Money money))
            return false;

        return amount.equals(money.amount)
                && currency == money.currency;

    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, currency);

    }

    @Override
    public String toString() {

        return currency + " " + amount;

    }


}