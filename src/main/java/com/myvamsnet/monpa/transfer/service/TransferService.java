package com.myvamsnet.monpa.transfer.service;


import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.transfer.dto.TransferRequest;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.service.TransactionService;
import com.myvamsnet.monpa.transfer.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final TransactionMapper transactionMapper;

    private final TransferValidator transferValidator;
    private final TransactionService transactionService;


    @Transactional
    public TransactionResponse transfer(

            String email,

            TransferRequest request
    ){

//        1) Load Sender

        User senderUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));


//        2) Load Sender Wallet

        Wallet senderWallet =
                walletRepository.findByUser(senderUser)
                        .orElseThrow(() ->
                                new RuntimeException("Wallet not found"));



//        3) Load Receiver

        Wallet receiverWallet =
                walletRepository.findByAccountNumber(
                                request.getDestinationAccountNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Destination wallet not found"));


//        4) Convert to Money

        Money amount = Money.of(

                request.getAmount(),

                senderWallet.getCurrency()

        );

//        5) Prevent Self Transfer and that wallets are active

        transferValidator.validate(
                senderWallet,
                receiverWallet,
                amount
        );

//        walletService.withdraw(
//                senderWallet,
//                amount
//        );
//
//        walletService.deposit(
//                receiverWallet,
//                amount
//        );

        //        5) Debit Sender

        senderWallet.withdraw(amount);

//        6) Credit Receiver

        receiverWallet.deposit(amount);

//        7) Save Both Wallets

        walletRepository.save(senderWallet);

        walletRepository.save(receiverWallet);

//        8) Record Transactions

        String senderDescription =
                "Transfer to " +
                        receiverWallet.getAccountNumber();

        String receiverDescription =
                "Transfer from " +
                        senderWallet.getAccountNumber();

        String transferReference =
                transactionService.generateTransferReference();

        Transaction senderTransaction =
                transactionService.recordTransferOut(
                        senderWallet,
                        amount,
                        senderDescription,
                        transferReference
                );

        transactionService.recordTransferIn(
                receiverWallet,
                amount,
                receiverDescription,
                transferReference
        );

        return transactionMapper.toResponse(senderTransaction);

    }

}

