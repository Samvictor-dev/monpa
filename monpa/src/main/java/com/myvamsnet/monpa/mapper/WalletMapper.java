package com.myvamsnet.monpa.mapper;

import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse toWalletResponse(Wallet wallet) {

        return new WalletResponse(
                wallet.getId(),
                wallet.getAccountNumber(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus(),
                wallet.getUser().getFirstName() + " " + wallet.getUser().getLastName()
        );
    }

}
