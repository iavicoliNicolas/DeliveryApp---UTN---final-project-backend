package com.deliveryapp.backend.store.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.filter.StoreFilter;

import java.util.List;
import java.util.Optional;

public interface IStoreService {
    PaginationResult<StoreResponseDTO> findAll(PaginationQuery paginationQuery, StoreFilter filter);
    List<StoreResponseDTO> findAll();
    StoreResponseDTO save(StoreRequestDTO storeRequestDTO);
    Optional<StoreResponseDTO> findById(Long id);
    StoreResponseDTO update (Long id, StoreRequestDTO storeRequestDTO);
    void deleteById(Long id);
}
