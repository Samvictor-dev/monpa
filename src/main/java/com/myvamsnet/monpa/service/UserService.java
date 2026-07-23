package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.mapper.UserMapper;
import com.myvamsnet.monpa.dto.user.CreateUserRequest;
import com.myvamsnet.monpa.dto.user.UpdateUserRequest;
import com.myvamsnet.monpa.dto.user.UserResponse;
import com.myvamsnet.monpa.common.exception.EmailAlreadyExistsException;
import com.myvamsnet.monpa.common.exception.UserNotFoundException;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.User;
import com.myvamsnet.monpa.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;


    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder, WalletService walletService) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

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

    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCountryName(request.getCountryName());

        User updatedUser = userRepository.save(user);

        return  userMapper.toUserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Long countUsers() {
        return userRepository.count();
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return  userMapper.toUserResponse(user);
    }


    public Page<UserResponse> getUsers(String keyword, Pageable pageable) {

        Page<User> users;

        if (keyword == null || keyword.isBlank()) {

            users = userRepository.findAll(pageable);

        } else {

            users = userRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword,
                            keyword,
                            pageable
                    );

        }

        return users.map(userMapper::toUserResponse);
    }




}


