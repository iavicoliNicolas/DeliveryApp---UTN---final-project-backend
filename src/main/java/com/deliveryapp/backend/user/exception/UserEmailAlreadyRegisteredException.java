package com.deliveryapp.backend.user.exception;

public class UserEmailAlreadyRegisteredException extends RuntimeException {
    public UserEmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
