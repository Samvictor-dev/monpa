package com.myvamsnet.monpa.dto.admin;

import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.Role;
import lombok.Data;

@Data
public class AdminUserFilter {

    private String search;

    private Role role;

    private AccountStatus accountStatus;

}
