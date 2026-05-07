package com.deliveryapp.backend.product.dto;

import com.deliveryapp.backend.product.enums.EProductStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageURL;
    private EProductStatus status;
    private Long storeId;
}
