package com.deliveryapp.backend.order.mapper;

import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderMapper {
    public static OrderResponseDTO toResponse(Order order) {

        OrderResponseDTO dto = OrderResponseDTO.builder()
                .id(order.getId())
                .storeId(order.getStore().getId())
                .consumerId(order.getConsumer().getId())
                .products(order.getProducts().stream().map(Product::getId).toList())
                .customerLongitude(order.getCustomerLongitude())
                .customerAddress(order.getCustomerAddress())
                .customerLatitude(order.getCustomerLatitude())
                .total(order.getTotal())
                .latitude(order.getLatitude())
                .longitude(order.getLongitude())
                .status(order.getStatus())
                .paymentType(order.getPaymentType())
                .build();

        Optional<User> rider = Optional.ofNullable(order.getRider());
        if (rider.isPresent()) {
            dto.setRiderId(order.getRider().getId());
        }else{
            dto.setRiderId(null);
        }

        return dto;
    }

    public static Order toEntity(CreateOrderRequestDTO orderRequestDTO, User consumer,
                                 Store store, BigDecimal total, List<Product> productList, EOrderStatus orderStatus, User rider) {

        return Order.builder()
                .products(productList)
                .customerAddress(orderRequestDTO.getCustomerAddress())
                .total(total)
                .status(orderStatus)
                .consumer(consumer)
                .rider(rider)
                .store(store)
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .customerLatitude(orderRequestDTO.getCustomerLatitude())
                .customerLongitude(orderRequestDTO.getCustomerLongitude())
                .paymentType(orderRequestDTO.getPaymentType())
                .build();
    }
}
