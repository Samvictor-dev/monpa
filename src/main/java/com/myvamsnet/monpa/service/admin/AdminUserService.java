package com.myvamsnet.monpa.service.admin;

import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.common.pagination.PageRequestFactory;
import com.myvamsnet.monpa.common.pagination.PaginationUtil;
import com.myvamsnet.monpa.dto.admin.AdminUserFilter;
import com.myvamsnet.monpa.dto.admin.AdminUserResponse;
import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.mapper.AdminUserMapper;
import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.AuditAction;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.repository.WalletRepository;
import com.myvamsnet.monpa.specification.AdminUserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final AdminUserMapper adminUserMapper;

    private final AuditService auditService;

    public PagedResponse<AdminUserResponse> getUsers(

            AdminUserFilter filter,

            int page,

            int size

    ) {

        Pageable pageable =
                PageRequestFactory.defaultPage(page, size);

        Page<User> users = userRepository.findAll(

                AdminUserSpecification.build(filter),

                pageable

        );

        Page<AdminUserResponse> responsePage =
                users.map(adminUserMapper::toResponse);

        return PaginationUtil.from(responsePage);

    }

    @Transactional
    public void freezeUser(

            Long id,

            String adminEmail

    ) {

        User user = userRepository.findById(id)

                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        user.setAccountStatus(AccountStatus.SUSPENDED);

        auditService.log(

                adminEmail,

                user.getEmail(),

                AuditAction.FREEZE_ACCOUNT,

                "Administrator suspended account"

        );

    }

    @Transactional
    public void activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        user.setAccountStatus(AccountStatus.ACTIVE);

    }

}