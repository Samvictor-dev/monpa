package com.myvamsnet.monpa.application.withdraw;

import com.myvamsnet.monpa.accounting.AccountingService;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.dto.wallet.WithdrawRequest;
import com.myvamsnet.monpa.mapper.WalletMapper;
import com.myvamsnet.monpa.model.Journal;
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
public class WithdrawTransactionService {

    private final WalletRepository walletRepository;

    private final WalletService walletService;

    private final AccountingService accountingService;

    private final TransactionService transactionService;

    private final WalletMapper walletMapper;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class
    )
    public WalletResponse execute(

            Long walletId,

            WithdrawRequest request

    ) {

        Wallet wallet =
                walletRepository.findById(walletId)
                        .orElseThrow(() ->
                                new WalletNotFoundException(walletId));

        Money amount =
                Money.of(
                        request.getAmount(),
                        wallet.getCurrency()
                );

        walletService.withdraw(
                wallet,
                amount
        );

        walletRepository.save(wallet);

        Journal journal =
                accountingService.recordWithdrawal(
                        wallet,
                        amount,
                        "Withdrawal from wallet "
                                + wallet.getAccountNumber()
                );

        String transactionReference =
                transactionService.generateTransactionReference();


                transactionService.recordWithdrawal(
                        wallet,
                        amount,
                        "Wallet Withdrawal",
                        transactionReference,
                        journal
                );

        return walletMapper.toWalletResponse(wallet);

    }

}
