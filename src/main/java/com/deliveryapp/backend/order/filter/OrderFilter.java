package com.deliveryapp.backend.order.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilter {

    private String status;
    private String paymentType;
    private Long consumerId;
    private BigDecimal totalMin;
    private BigDecimal totalMax;

}
