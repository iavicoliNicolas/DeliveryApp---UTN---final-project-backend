package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.order.dto.OrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderResponseDTO> findAll();
    OrderResponseDTO save(OrderRequestDTO dto);
    Optional<OrderResponseDTO> findById(Long id);
    OrderResponseDTO update (Long id, OrderRequestDTO orderRequestDTO);
    void deleteById(Long id);
}
