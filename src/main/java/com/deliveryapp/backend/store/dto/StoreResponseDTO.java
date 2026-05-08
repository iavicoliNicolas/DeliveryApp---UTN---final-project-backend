package com.deliveryapp.backend.store.dto;

import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.user.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String address;
    private User owner;
    private Set<Product> products;
}
