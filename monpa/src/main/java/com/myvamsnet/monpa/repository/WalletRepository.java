package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByAccountNumber(String accountNumber);

    Optional<Wallet> findByUserId(Long userId);

    Optional<Wallet> findByUser(User user);

}