package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.order.dto.OrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    @Override
    public List<OrderResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public OrderResponseDTO save(OrderRequestDTO orderRequestDTO) {
        return null;
    }

    @Override
    public Optional<OrderResponseDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
