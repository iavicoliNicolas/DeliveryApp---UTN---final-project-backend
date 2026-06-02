package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderLocationRequestDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderStatusRequestDTO;
import com.deliveryapp.backend.order.filter.OrderFilter;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    PaginationResult<OrderResponseDTO> findByStoreId(Long storeId, PaginationQuery paginationQuery, OrderFilter orderFilter);
    List<OrderResponseDTO> findAll();
    OrderResponseDTO save(CreateOrderRequestDTO dto);
    Optional<OrderResponseDTO> findById(Long id);
    OrderResponseDTO update(Long id, CreateOrderRequestDTO createOrderRequestDTO);
    OrderResponseDTO updateMerchantOrderStatus(Long id, @Valid UpdateOrderStatusRequestDTO dto);
    OrderResponseDTO updateRiderOrderStatus(Long id, @Valid UpdateOrderStatusRequestDTO dto);
    OrderResponseDTO updateOrderLocation(Long id, @Valid UpdateOrderLocationRequestDTO dto);
    void deleteById(Long id);
    List<OrderResponseDTO> findUnassignedOrders();
    List<OrderResponseDTO> findMyOrders();
}
