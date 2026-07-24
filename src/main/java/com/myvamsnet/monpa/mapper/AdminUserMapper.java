package com.myvamsnet.monpa.mapper;

import com.myvamsnet.monpa.dto.admin.AdminUserResponse;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper {

    public AdminUserResponse toResponse(User user) {

        Wallet wallet = user.getWallet();

        return AdminUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .createdAt(user.getCreatedAt())
                .walletNumber(wallet != null ? wallet.getAccountNumber() : null)
                .walletBalance(wallet != null ? wallet.getBalance() : null)
                .walletStatus(wallet != null ? wallet.getStatus() : null)
                .build();
    }

}
