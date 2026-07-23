package com.myvamsnet.monpa.dto.transaction;

import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistoryResponse {

    private String transactionReference;

    private String transferReference;

    private TransactionType type;

    private TransactionStatus status;

    private BigDecimal amount;

    private BigDecimal balanceAfterTransaction;

    private String description;

    private LocalDateTime createdAt;

}
