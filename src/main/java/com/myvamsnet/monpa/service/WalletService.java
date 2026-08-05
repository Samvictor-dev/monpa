package com.myvamsnet.monpa.service;


import com.myvamsnet.monpa.accounting.AccountingService;
import com.myvamsnet.monpa.common.concurrency.OptimisticLockExecutor;
import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.dto.wallet.DepositRequest;
import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.dto.wallet.WithdrawRequest;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.mapper.WalletMapper;
import com.myvamsnet.monpa.model.*;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final WalletMapper walletMapper;

    private final TransactionMapper transactionMapper;

    private final TransactionService transactionService;

    private final AccountingService accountingService;

    private final OptimisticLockExecutor optimisticLockExecutor;


    @Transactional
    public void createWallet(User user) {

        Wallet wallet =
                Wallet.create(user, generateAccountNumber());

        walletRepository.save(wallet);

    }


//    @Transactional
//    public Wallet createWallet(User user) {
//
//        String accountNumber = generateAccountNumber();
//
//        Wallet wallet = Wallet.createFor(user, accountNumber);
//
//        return walletRepository.save(wallet);
//    }

    @Transactional(readOnly = true)
    public WalletResponse getMyWallet(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return walletMapper.toWalletResponse(wallet);
    }


    private String generateAccountNumber() {

        long count = walletRepository.count() + 1;

        return "MM" + String.format("%08d", count);

    }


    @Transactional(readOnly = true)
    public WalletResponse getWalletByUserId(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new WalletNotFoundException(userId));

        return walletMapper.toWalletResponse(wallet);

    }

    @Transactional
    public TransactionResponse deposit(
            String email,
            DepositRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(email));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() ->
                        new WalletNotFoundException(user.getId()));

        return optimisticLockExecutor.execute(
                () -> performDeposit(
                        wallet.getId(),
                        request
                )
        );

    }

    private TransactionResponse performDeposit(

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

        wallet.deposit(
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

        String transactionReference =
                transactionService.generateTransactionReference();

        Transaction transaction =
                transactionService.recordDeposit(
                        wallet,
                        depositAmount,
                        request.getDescription(),
                        transactionReference
                );

        transaction.setJournal(
                journal
        );

        return transactionMapper.toResponse(
                transaction
        );

    }

//    @Transactional
//    public TransactionResponse deposit(
//            String email,
//            DepositRequest request
//    ) {
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Wallet wallet = walletRepository.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Wallet not found"));
//
//
//        Money depositAmount = Money.of(
//                request.getAmount(),
//                wallet.getCurrency()
//        );
//
//        Journal journal = accountingService.recordDeposit(
//                wallet,
//                depositAmount,
//                "Deposit into wallet "
//                        + wallet.getAccountNumber()
//        );
//
//        wallet.deposit(depositAmount);
//
//        walletRepository.save(wallet);
//
//        String transactionReference = transactionService.generateTransactionReference();
//
//        Transaction transaction =
//                transactionService.recordDeposit(
//                        wallet,
//                        depositAmount,
//                        request.getDescription(),
//                        transactionReference
//                );
//
//        transaction.setJournal(journal);
//
//        return transactionMapper.toResponse(transaction);
//
//    }

    @Transactional
    public WalletResponse freezeWallet(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.freeze();

        walletRepository.save(wallet);

        return walletMapper.toWalletResponse(wallet);

    }

    @Transactional
    public WalletResponse unfreezeWallet(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.unfreeze();

        walletRepository.save(wallet);

        return walletMapper.toWalletResponse(wallet);

    }

    @Transactional
    public WalletResponse withdraw(
            Long userId,
            WithdrawRequest request
    ) {

        Wallet wallet =
                walletRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new WalletNotFoundException(userId));

        return optimisticLockExecutor.execute(

                () -> performWithdrawal(

                        wallet.getId(),

                        request

                )

        );

    }

    private WalletResponse performWithdrawal(

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

        wallet.withdraw(amount);

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

        Transaction transaction =
                transactionService.recordWithdrawal(
                        wallet,
                        amount,
                        "Wallet Withdrawal",
                        transactionReference
                );

        transaction.setJournal(journal);

        return walletMapper.toWalletResponse(wallet);

    }

//    @Transactional
//    public WalletResponse withdraw(
//            Long userId,
//            WithdrawRequest request
//    ) {
//
//        Wallet wallet = walletRepository.findByUserId(userId)
//                .orElseThrow(() ->
//                        new WalletNotFoundException(userId));
//
//        Money withdrawal = Money.of(
//                request.getAmount(),
//                wallet.getCurrency()
//        );
//
//        Money money = Money.of(
//                request.getAmount(),
//                wallet.getCurrency()
//        );
//
//        Journal journal =
//                accountingService.recordWithdrawal(
//                        wallet,
//                        money,
//                        "Withdrawal from wallet "
//                                + wallet.getAccountNumber()
//                );
//
//        wallet.withdraw(withdrawal);
//
//        walletRepository.save(wallet);
//
//        String transactionReference =
//                transactionService.generateTransactionReference();
//
//        Transaction transaction =
//                transactionService.recordWithdrawal(
//                wallet,
//                withdrawal,
//                "Wallet Withdrawal",
//                        transactionReference
//        );
//
//        transaction.setJournal(journal);
//
//        return walletMapper.toWalletResponse(wallet);
//
//    }

}