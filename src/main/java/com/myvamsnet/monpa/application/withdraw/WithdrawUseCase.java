package com.myvamsnet.monpa.application.withdraw;

import com.myvamsnet.monpa.common.concurrency.OptimisticLockExecutor;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.dto.wallet.WithdrawRequest;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawUseCase {

    private final WalletRepository walletRepository;

    private final WithdrawTransactionService withdrawTransactionService;

    private final OptimisticLockExecutor optimisticLockExecutor;

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

                () -> withdrawTransactionService.execute(

                        wallet.getId(),

                        request

                )

        );

    }

}
