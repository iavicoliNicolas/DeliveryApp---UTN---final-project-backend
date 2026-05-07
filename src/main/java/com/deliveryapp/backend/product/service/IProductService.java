package com.deliveryapp.backend.product.service;

import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<ProductResponseDTO> findAll();
    ProductResponseDTO save(ProductRequestDTO productRequestDTO);
    Optional<ProductResponseDTO> findById(Long id);
    ProductResponseDTO update (Long id, ProductRequestDTO productRequestDTO);
    void deleteById(Long id);



}
