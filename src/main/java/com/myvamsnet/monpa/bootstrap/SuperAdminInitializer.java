package com.myvamsnet.monpa.bootstrap;

import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer
        implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${superadmin.email}")
    private String email;

    @Value("${superadmin.password}")
    private String password;

    @Value("${superadmin.firstname}")
    private String firstName;

    @Value("${superadmin.lastname}")
    private String lastName;

    @Value("${superadmin.phone}")
    private String phone;

    @Value("${superadmin.country}")
    private String country;

    private static final Logger log =
            LoggerFactory.getLogger(
                    SuperAdminInitializer.class
            );

    @Override
    public void run(String... args) {

        if (userRepository.existsByRole(Role.SUPER_ADMIN)) {
            return;
        }

        User admin = new User();

        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);

        admin.setPassword(
                passwordEncoder.encode(password)
        );

        admin.setRole(Role.SUPER_ADMIN);

        admin.setAccountStatus(AccountStatus.ACTIVE);

        admin.setPhoneNumber(phone);
        admin.setCountryName(country);

        userRepository.save(admin);

        log.info("Super admin created: {}", email);

//        System.out.println("==================================");
//        System.out.println("SUPER ADMIN CREATED");
//        System.out.println(email);
//        System.out.println("==================================");

    }

}
