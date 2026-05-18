package com.deliveryapp.backend.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
