package com.deliveryapp.backend.order.mapper;

import com.deliveryapp.backend.order.dto.OrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {
    public static OrderResponseDTO toResponse(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setRiderId(order.getRider().getId());
        dto.setStoreId(order.getStore().getId());
        dto.setConsumerId(order.getConsumer().getId());
        dto.setProducts(order.getProducts().stream().map(Product::getId).toList());
        dto.setOrderAddress(order.getOrderAddress());
        dto.setStoreAddress(order.getStoreAddress());
        dto.setTotal(order.getTotal());
        dto.setLocation(order.getLocation());
        dto.setStatus(order.getStatus());

        return dto;
    }

    public static Order toEntity(OrderRequestDTO orderRequestDTO, User consumer, Store store, BigDecimal total, List<Product> products) {
        Order order = new Order();
        order.setProducts(products);
        order.setOrderAddress(orderRequestDTO.getOrderAddress());
        order.setStoreAddress(store.getAddress());
        order.setTotal(total);
        order.setLocation(null);
        order.setStatus(EOrderStatus.PENDING);
        order.setConsumer(consumer);
        order.setRider(null);
        order.setStore(store);

        return order;
    }
}
