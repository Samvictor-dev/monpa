package com.myvamsnet.monpa.application.transfer;

import com.myvamsnet.monpa.common.concurrency.OptimisticLockExecutor;
import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.transfer.dto.TransferRequest;
import com.myvamsnet.monpa.transfer.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferUseCase {

    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final TransferTransactionService transferTransactionService;

    private final TransferValidator transferValidator;

    private final OptimisticLockExecutor optimisticLockExecutor;

    @Transactional
    public TransactionResponse transfer(

            String email,

            TransferRequest request

    ) {

        User senderUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(email));

        Wallet senderWallet =
                walletRepository.findByUser(senderUser)
                        .orElseThrow(() ->
                                new WalletNotFoundException(
                                        senderUser.getId()
                                ));

        Wallet receiverWallet =
                walletRepository.findByAccountNumber(
                                request.getDestinationAccountNumber()
                        )
                        .orElseThrow(() ->
                                new WalletNotFoundException(
                                        request.getDestinationAccountNumber()
                                ));

        Money amount =
                Money.of(
                        request.getAmount(),
                        senderWallet.getCurrency()
                );

        transferValidator.validate(
                senderWallet,
                receiverWallet,
                amount
        );

        return optimisticLockExecutor.execute(

                () ->
                        transferTransactionService.execute(

                                senderWallet.getId(),

                                receiverWallet.getId(),

                                request

                        )

        );

    }

}
