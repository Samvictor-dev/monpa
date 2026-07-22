package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Pageable pageable
    );

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
