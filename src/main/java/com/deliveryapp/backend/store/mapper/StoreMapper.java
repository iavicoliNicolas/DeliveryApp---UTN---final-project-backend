package com.deliveryapp.backend.store.mapper;

import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;

public class StoreMapper {
    public static StoreResponseDTO toResponse(Store store) {

       return StoreResponseDTO.builder()
               .id(store.getId())
               .name(store.getName())
               .address(store.getAddress())
               .ownerId(store.getOwner().getId())
               .latitude(store.getLatitude())
               .longitude(store.getLongitude())
               .build();
    }

    public static Store toEntity(StoreRequestDTO dto, User user) {

        return Store.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .owner(user)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
}
