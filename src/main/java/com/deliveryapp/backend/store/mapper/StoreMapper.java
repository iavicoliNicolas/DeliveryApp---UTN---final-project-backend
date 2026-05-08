package com.deliveryapp.backend.store.mapper;

import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;

public class StoreMapper {
    public static StoreResponseDTO toResponse(Store store) {
        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setAddress(store.getAddress());
        dto.setOwnerId(store.getOwner().getId());

        return dto;
    }

    public static Store toEntity(StoreRequestDTO dto, User user) {
        Store store = new Store();
        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setOwner(user);

        return store;
    }
}
