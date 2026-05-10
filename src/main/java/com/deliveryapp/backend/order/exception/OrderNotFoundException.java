package com.deliveryapp.backend.order.exception;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(Long id) {
        super("Order not found with id " + id);
    }
}
