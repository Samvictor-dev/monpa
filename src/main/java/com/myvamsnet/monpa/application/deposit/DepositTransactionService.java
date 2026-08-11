package com.myvamsnet.monpa.application.deposit;

import com.myvamsnet.monpa.accounting.AccountingService;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.wallet.DepositRequest;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.service.TransactionService;
import com.myvamsnet.monpa.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositTransactionService {

    private final WalletRepository walletRepository;

    private final WalletService walletService;

    private final AccountingService accountingService;

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public TransactionResponse execute(

            Long walletId,

            DepositRequest request

    ) {

        Wallet wallet =
                walletRepository.findById(walletId)
                        .orElseThrow(() ->
                                new WalletNotFoundException(walletId));

        Money depositAmount =
                Money.of(
                        request.getAmount(),
                        wallet.getCurrency()
                );

        walletService.deposit(
                wallet,
                depositAmount
        );

        walletRepository.save(wallet);

        Journal journal =
                accountingService.recordDeposit(
                        wallet,
                        depositAmount,
                        "Deposit into wallet "
                                + wallet.getAccountNumber()
                );

        if ("ROLLBACK_TEST".equals(request.getDescription())) {
            throw new RuntimeException("Testing rollback");
        }

        String transactionReference =
                transactionService.generateTransactionReference();

        Transaction transaction =
                transactionService.recordDeposit(
                        wallet,
                        depositAmount,
                        request.getDescription(),
                        transactionReference
                );

        transaction.attachJournal(journal);

        return transactionMapper.toResponse(transaction);

    }

}
