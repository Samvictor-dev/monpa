package com.myvamsnet.monpa.dto.transaction;

import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TransactionFilter {

    private TransactionType type;

    private TransactionStatus status;

    private String reference;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

}