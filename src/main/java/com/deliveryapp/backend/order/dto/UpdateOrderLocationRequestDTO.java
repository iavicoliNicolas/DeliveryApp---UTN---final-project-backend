package com.deliveryapp.backend.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderLocationRequestDTO {

    @NotNull(message = "La latitud del pedido no puede ser nula")
    private BigDecimal latitude;

    @NotNull(message = "La longitud de la entrega no puede ser nula")
    private BigDecimal longitude;

}
