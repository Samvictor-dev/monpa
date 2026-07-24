package com.myvamsnet.monpa.dto.transaction;

import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminTransactionFilter {

    private TransactionType type;

    private TransactionStatus status;

    private String transactionReference;

    private String transferReference;

    private String email;

    private String walletNumber;

    private BigDecimal minimumAmount;

    private BigDecimal maximumAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

}