package com.deliveryapp.backend.order.mapper;

import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.product.model.Product;

public class OrderMapper {
    public static OrderResponseDTO toResponse(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setRiderId(order.getRider().getId());
        orderResponseDTO.setStoreId(order.getStore().getId());
        orderResponseDTO.setConsumerId(order.getConsumer().getId());
        orderResponseDTO.setProducts(order.getProducts().stream().map(Product::getId).toList());
        orderResponseDTO.setOrderAddress(order.getOrderAddress());
        orderResponseDTO.setStoreAddress(order.getStoreAddress());
        orderResponseDTO.setTotal(order.getTotal());
        orderResponseDTO.setLocation(order.getLocation());
        orderResponseDTO.setStatus(order.getStatus());

        return orderResponseDTO;
    }


}
