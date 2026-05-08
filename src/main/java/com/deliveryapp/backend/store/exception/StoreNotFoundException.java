package com.deliveryapp.backend.store.exception;

public class StoreNotFoundException extends StoreException {
    public StoreNotFoundException(Long id) {
        super("Store not found with id " + id);
    }
}
