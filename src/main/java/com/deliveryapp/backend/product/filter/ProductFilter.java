package com.deliveryapp.backend.product.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductFilter {
    private String name;
    private String description;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String status;
    private Long storeId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer distance;

}
