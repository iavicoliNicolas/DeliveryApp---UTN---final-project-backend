package com.deliveryapp.backend.product.exception;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(Long id) {
        super("No se encuentra producto con id: " + id);
    }
}
