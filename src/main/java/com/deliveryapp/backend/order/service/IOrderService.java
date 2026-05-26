package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderRequestDTO;
import com.deliveryapp.backend.order.filter.OrderFilter;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    PaginationResult<OrderResponseDTO> findByStoreId(Long storeId, PaginationQuery paginationQuery, OrderFilter orderFilter);
    List<OrderResponseDTO> findAll();
    OrderResponseDTO save(CreateOrderRequestDTO dto);
    Optional<OrderResponseDTO> findById(Long id);
    OrderResponseDTO update (Long id, CreateOrderRequestDTO createOrderRequestDTO);
    OrderResponseDTO updateStatus (Long id, UpdateOrderRequestDTO updateOrderRequestDTO);
    void deleteById(Long id);
    List<OrderResponseDTO> findByStoreId(Long storeId);
    List<OrderResponseDTO> findUnassignedOrders();
}
