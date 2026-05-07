package com.deliveryapp.backend.product.mapper;

import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.store.model.Store;

public class ProductMapper {
    public static ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImageURL(product.getImageURL());
        dto.setStatus(product.getStatus());
        dto.setStoreId(product.getStore().getId());

        return dto;
    }

    public static Product toEntity(ProductRequestDTO dto, Store store) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageURL(dto.getImageURL());
        product.setStatus(dto.getStatus());
        product.setStore(store);

        return product;
    }

}
