package com.myvamsnet.monpa.application.deposit;

import com.myvamsnet.monpa.common.concurrency.OptimisticLockExecutor;
import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.dto.wallet.DepositRequest;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositUseCase {

    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final DepositTransactionService depositTransactionService;

    private final OptimisticLockExecutor optimisticLockExecutor;

    @Transactional
    public TransactionResponse deposit(
            String email,
            DepositRequest request
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(email));

        Wallet wallet =
                walletRepository.findByUser(user)
                        .orElseThrow(() ->
                                new WalletNotFoundException(user.getId()));

        return optimisticLockExecutor.execute(

                () -> depositTransactionService.execute(

                        wallet.getId(),

                        request

                )

        );

    }

}
