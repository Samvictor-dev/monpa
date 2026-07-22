package com.myvamsnet.monpa.dto.wallet;

import com.myvamsnet.monpa.common.valueobject.Currency;
import com.myvamsnet.monpa.model.WalletStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class WalletResponse {

    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private Currency currency;

    private WalletStatus status;

    private String ownerName;

    public WalletResponse() {
    }

    public WalletResponse(
            Long id,
            String accountNumber,
            BigDecimal balance,
            Currency currency,
            WalletStatus status,
            String ownerName
    ) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
        this.ownerName = ownerName;
    }

    // getters and setters
}