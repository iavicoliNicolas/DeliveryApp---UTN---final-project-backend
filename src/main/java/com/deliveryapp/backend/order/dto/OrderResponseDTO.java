package com.deliveryapp.backend.order.dto;

import com.deliveryapp.backend.location.model.Location;
import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.user.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long riderId;
    private Long storeId;
    private Long consumerId;
    private List<Long> products;
    private String storeAddress;
    private String orderAddress;
    private BigDecimal total;
    private Location location;
    private EOrderStatus status;
}
