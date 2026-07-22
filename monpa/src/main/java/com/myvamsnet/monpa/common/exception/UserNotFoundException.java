package com.myvamsnet.monpa.common.exception;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("User with ID " + id + " was not found.");
    }

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' was not found.");
    }
}
