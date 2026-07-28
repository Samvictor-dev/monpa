package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.common.exception.InvalidRoleOperationException;
import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.AuditAction;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRoleService {

    private final UserRepository userRepository;

    private final AuditService auditService;

    @Transactional
    public void promoteToAdmin(

            Long id,

            String actorEmail

    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new InvalidRoleOperationException("User not found"));

        if (user.getEmail().equals(actorEmail)) {

            throw new InvalidRoleOperationException(
                    "You cannot modify your own role."
            );

        }

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {

            throw new InvalidRoleOperationException(
                    "Only active users can become administrators."
            );

        }

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidRoleOperationException("User is already an admin");
        }

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new InvalidRoleOperationException("User is already a super admin");
        }

        auditService.log(

                actorEmail,

                user.getEmail(),

                AuditAction.PROMOTE_ADMIN,

                "User promoted to ADMIN"

        );

        user.setRole(Role.ADMIN);

    }

    @Transactional
    public void demoteAdmin(

            Long id,

            String actorEmail

    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new InvalidRoleOperationException("User not found"));

        if (user.getEmail().equals(actorEmail)) {

            throw new InvalidRoleOperationException(
                    "You cannot modify your own role."
            );

        }

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {

            throw new InvalidRoleOperationException(
                    "Only active users can become administrators."
            );

        }

        if (user.getRole() == Role.USER) {
            throw new InvalidRoleOperationException("User is already a normal user");
        }

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new InvalidRoleOperationException(
                    "Super Admin cannot be demoted here"
            );
        }

        auditService.log(

                actorEmail,

                user.getEmail(),

                AuditAction.DEMOTE_ADMIN,

                "Administrator privileges removed"

        );

        user.setRole(Role.USER);

    }

}
