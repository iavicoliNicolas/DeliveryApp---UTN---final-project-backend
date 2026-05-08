package com.deliveryapp.backend.store.dto;

import lombok.Data;

@Data
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String address;
    private Long ownerId;
}
