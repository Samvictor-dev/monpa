package com.myvamsnet.monpa.application.transfer;

import com.myvamsnet.monpa.accounting.AccountingService;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.service.TransactionService;
import com.myvamsnet.monpa.service.WalletService;
import com.myvamsnet.monpa.transfer.dto.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferTransactionService {

    private final WalletRepository walletRepository;

    private final AccountingService accountingService;

    private final WalletService walletService;

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public TransactionResponse execute(

            Long senderWalletId,

            Long receiverWalletId,

            TransferRequest request

    ) {

        Wallet senderWallet =
                walletRepository.findById(senderWalletId)
                        .orElseThrow(() ->
                                new WalletNotFoundException(senderWalletId));

        Wallet receiverWallet =
                walletRepository.findById(receiverWalletId)
                        .orElseThrow(() ->
                                new WalletNotFoundException(receiverWalletId));

        Money amount =
                Money.of(
                        request.getAmount(),
                        senderWallet.getCurrency()
                );

        walletService.withdraw(
                senderWallet,
                amount
        );

        walletService.deposit(
                receiverWallet,
                amount
        );

        walletRepository.save(senderWallet);

        walletRepository.save(receiverWallet);

        Journal journal =
                accountingService.recordTransfer(
                        senderWallet,
                        receiverWallet,
                        amount,
                        "Transfer from "
                                + senderWallet.getAccountNumber()
                                + " to "
                                + receiverWallet.getAccountNumber()
                );

        String transferReference =
                transactionService.generateTransferReference();

        Transaction senderTransaction =
                transactionService.recordTransferOut(
                        senderWallet,
                        amount,
                        "Transfer to "
                                + receiverWallet.getAccountNumber(),
                        transferReference
                );

        Transaction receiverTransaction =
                transactionService.recordTransferIn(
                        receiverWallet,
                        amount,
                        "Transfer from "
                                + senderWallet.getAccountNumber(),
                        transferReference
                );

        senderTransaction.setJournal(journal);

        receiverTransaction.setJournal(journal);

        return transactionMapper.toResponse(senderTransaction);

    }

}
