package com.myvamsnet.monpa.controller;

import com.myvamsnet.monpa.dto.auth.AuthenticationRequest;
import com.myvamsnet.monpa.dto.auth.AuthenticationResponse;
import com.myvamsnet.monpa.dto.common.ApiResponse;
import com.myvamsnet.monpa.dto.user.CreateUserRequest;
import com.myvamsnet.monpa.dto.user.UserResponse;
import com.myvamsnet.monpa.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse login(
            @Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    //User can't register yet

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse response =
                authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<UserResponse>builder()
                                .success(true)
                                .message("User created successfully")
                                .data(response)
                                .build()
                );
    }
}
