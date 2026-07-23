package com.myvamsnet.monpa.dto.transaction;

import com.myvamsnet.monpa.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {

    private Long id;

    private String transactionReference;

    private String transferReference;

    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal balanceAfterTransaction;

    private String description;

    private LocalDateTime createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            String transactionReference,
            String transferReference,
            TransactionType type,
            BigDecimal amount,
            BigDecimal balanceAfterTransaction,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.transactionReference = transactionReference;
        this.transferReference = transferReference;
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.description = description;
        this.createdAt = createdAt;
    }

}