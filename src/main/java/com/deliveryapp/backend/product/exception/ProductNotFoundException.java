package com.deliveryapp.backend.product.exception;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(Long id) {
      super("Product not found with id " + id);
    }
}
