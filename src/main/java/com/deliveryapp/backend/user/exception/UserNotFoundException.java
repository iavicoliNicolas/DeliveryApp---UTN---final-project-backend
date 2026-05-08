package com.deliveryapp.backend.user.exception;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(Long id) {
        super("User not found with id " + id);
    }
}
