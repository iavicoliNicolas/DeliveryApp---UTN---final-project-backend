package com.deliveryapp.backend.order.dto;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import jakarta.validation.constraints.NotNull;
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
public class UpdateOrderRequestDTO {

    @NotNull(message = "El estado del pedido no puede ser nulo")
    private EOrderStatus status;

    @NotNull(message = "La latitud del pedido no puede ser nula")
    private BigDecimal latitude;

    @NotNull(message = "La longitud de la entrega no puede ser nula")
    private BigDecimal longitude;

}
