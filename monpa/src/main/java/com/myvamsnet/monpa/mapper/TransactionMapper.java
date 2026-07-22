package com.myvamsnet.monpa.mapper;

import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getTransferReference(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }

}

