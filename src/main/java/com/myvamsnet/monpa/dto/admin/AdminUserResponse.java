package com.myvamsnet.monpa.dto.admin;

import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.WalletStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminUserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

    private AccountStatus accountStatus;

    private String walletNumber;

    private BigDecimal walletBalance;

    private WalletStatus walletStatus;

    private LocalDateTime createdAt;

}