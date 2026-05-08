package com.deliveryapp.backend.store.service;

import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IStoreService {
    List<StoreResponseDTO> findAll();
    StoreResponseDTO save(StoreRequestDTO storeRequestDTO);
    Optional<StoreResponseDTO> findById(Long id);
    StoreResponseDTO update (Long id, StoreRequestDTO storeRequestDTO);
    void deleteById(Long id);
}
