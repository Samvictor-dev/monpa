package com.myvamsnet.monpa.service;


import com.myvamsnet.monpa.common.valueobject.Money;
import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.common.exception.WalletNotFoundException;
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


    @Transactional
    public void createWallet(User user) {

        Wallet wallet =
                Wallet.create(user, generateAccountNumber());

        walletRepository.save(wallet);

    }

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

    public void deposit(
            Wallet wallet,
            Money money
    ) {
        wallet.deposit(money);
    }

    public void withdraw(
            Wallet wallet,
            Money money
    ) {
        wallet.withdraw(money);
    }

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

}