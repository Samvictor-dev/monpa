package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.dto.auth.AuthenticationRequest;
import com.myvamsnet.monpa.dto.auth.AuthenticationResponse;
import com.myvamsnet.monpa.dto.user.CreateUserRequest;
import com.myvamsnet.monpa.dto.user.UserResponse;
import com.myvamsnet.monpa.common.exception.EmailAlreadyExistsException;
import com.myvamsnet.monpa.mapper.UserMapper;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.repository.UserRepository;
import com.myvamsnet.monpa.security.CustomUserDetails;
import com.myvamsnet.monpa.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwt = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        UserResponse response = userMapper.toUserResponse(user);

        return new AuthenticationResponse(
                jwt,
                "Bearer",
                response
        );

    }

    @Transactional
    public UserResponse register(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountryName(request.getCountryName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);


        User savedUser = userRepository.save(user);

        walletService.createWallet(savedUser);

        return userMapper.toUserResponse(savedUser);
    }

}
