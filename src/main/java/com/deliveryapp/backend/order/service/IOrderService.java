package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderResponseDTO> findAll();
    OrderResponseDTO save(CreateOrderRequestDTO dto);
    Optional<OrderResponseDTO> findById(Long id);
    OrderResponseDTO update (Long id, CreateOrderRequestDTO createOrderRequestDTO);
    OrderResponseDTO updateStatus (Long id, UpdateOrderRequestDTO updateOrderRequestDTO);
    void deleteById(Long id);
    List<OrderResponseDTO> findByStoreId(Long storeId);
    List<OrderResponseDTO> findUnassignedOrders();
}
