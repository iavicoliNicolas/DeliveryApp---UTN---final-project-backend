package com.deliveryapp.backend.order.filter;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderFilter {

    private Long id;
    private Long riderId;
    private Long consumerId;
    private Long storeId;
    private String customerAddress;
    private BigDecimal totalMin;
    private BigDecimal totalMax;
    private EOrderStatus status;
    private EPaymentType paymentType;
    private LocalDateTime lastUpdateFrom;
    private LocalDateTime lastUpdateTo;
}