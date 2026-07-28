package com.myvamsnet.monpa.service;

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

    @Transactional
    public void promoteToAdmin(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("User is already an admin");
        }

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new RuntimeException("User is already a super admin");
        }

        user.setRole(Role.ADMIN);

    }

    @Transactional
    public void demoteAdmin(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getRole() == Role.USER) {
            throw new RuntimeException("User is already a normal user");
        }

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new RuntimeException(
                    "Super Admin cannot be demoted here"
            );
        }

        user.setRole(Role.USER);

    }

}
