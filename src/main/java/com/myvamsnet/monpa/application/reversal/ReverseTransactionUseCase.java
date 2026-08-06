package com.myvamsnet.monpa.application.reversal;

import com.myvamsnet.monpa.common.concurrency.OptimisticLockExecutor;
import com.myvamsnet.monpa.common.exception.TransactionNotFoundException;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReverseTransactionUseCase {

    private final TransactionRepository transactionRepository;

    private final OptimisticLockExecutor optimisticLockExecutor;

    private final ReverseTransactionExecutionService executionService;

    public TransactionResponse reverse(

            String transactionReference

    ) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionReference(
                                transactionReference
                        )
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        transactionReference
                                ));

        return optimisticLockExecutor.execute(

                () -> executionService.execute(

                        transaction.getTransactionReference()

                )

        );

    }

}
