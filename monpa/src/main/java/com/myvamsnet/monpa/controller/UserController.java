package com.myvamsnet.monpa.controller;

import com.myvamsnet.monpa.dto.common.ApiResponse;
import com.myvamsnet.monpa.dto.user.*;
import com.myvamsnet.monpa.service.UserService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse response =
                userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<UserResponse>builder()
                                .success(true)
                                .message("User created successfully with assigned wallet")
                                .data(response)
                                .build()
                );
    }


    @GetMapping("/count")
    public Long countUsers() {
        return userService.countUsers();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserResponse getUserById(
            @PathVariable Long id
    ) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById (
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(

            @RequestParam(required = false)
            String keyword,

            @PageableDefault(size = 10)
            Pageable pageable

    ) {

        Page<UserResponse> response =
                userService.getUsers(keyword, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<Page<UserResponse>>builder()
                                .success(true)
                                .message("Users retrieved successfully")
                                .data(response)
                                .build()
                );

    }

}
