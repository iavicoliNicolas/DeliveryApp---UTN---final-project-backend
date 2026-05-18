package com.deliveryapp.backend.order.dto;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;
    private Long riderId;
    private Long storeId;
    private Long consumerId;
    private List<Long> products;
    private String orderAddress;
    private BigDecimal total;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private EOrderStatus status;
    private EPaymentType paymentType;
}
