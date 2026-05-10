package com.deliveryapp.backend.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "La dirección de entrega no puede ser nula")
    private String orderAddress;

    @NotNull(message = "Debe haber mínimo un producto")
    private List<Long> products;

}
