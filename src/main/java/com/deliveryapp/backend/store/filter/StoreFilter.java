package com.deliveryapp.backend.store.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreFilter {
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer distance;
}
