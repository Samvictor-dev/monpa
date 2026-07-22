package com.myvamsnet.monpa.transfer.validation;

import com.myvamsnet.monpa.common.exception.*;
import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.model.Wallet;
import com.myvamsnet.monpa.model.WalletStatus;
import org.springframework.stereotype.Component;

@Component
public class TransferValidator {

    public void validate(
            Wallet sender,
            Wallet receiver,
            Money amount) {

        validateSender(sender);

        validateReceiver(receiver);

        validateSelfTransfer(sender, receiver);

        validateCurrency(sender, receiver);

        validateAmount(amount);

        validateBalance(sender, amount);
    }

    private void validateSender(Wallet wallet) {

        if (wallet.getStatus() != WalletStatus.ACTIVE) {

            throw new WalletNotActiveException(
                    wallet.getAccountNumber()
            );
        }
    }

    private void validateReceiver(Wallet wallet) {

        if (wallet.getStatus() != WalletStatus.ACTIVE) {

            throw new WalletNotActiveException(
                    wallet.getAccountNumber()
            );
        }
    }

    private void validateSelfTransfer(
            Wallet sender,
            Wallet receiver) {

        if (sender.getId().equals(receiver.getId())) {

            throw new SelfTransferNotAllowedException();
        }
    }

    private void validateCurrency(
            Wallet sender,
            Wallet receiver) {

        if (!sender.getCurrency().equals(receiver.getCurrency())) {

            throw new CurrencyMismatchException();
        }
    }

    private void validateAmount(Money amount) {

        if (!amount.isPositive()) {

            throw new InvalidAmountException();
        }
    }

    private void validateBalance(
            Wallet sender,
            Money amount) {

        if (!sender.hasSufficientBalance(amount)) {

            throw new InsufficientFundsException(
                    sender.getAccountNumber(),
                    amount
            );
        }
    }

}
