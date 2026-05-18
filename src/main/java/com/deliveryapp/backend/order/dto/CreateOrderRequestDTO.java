package com.deliveryapp.backend.order.dto;

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
public class CreateOrderRequestDTO {

    @NotNull(message = "La dirección de entrega no puede ser nula")
    private String orderAddress;

    @NotNull(message = "Debe haber mínimo un producto")
    private List<Long> products;

    @NotNull(message = "La latitud de la entrega no puede ser nula")
    private BigDecimal latitude;

    @NotNull(message = "La longitud de la entrega no puede ser nula")
    private BigDecimal longitude;

    @NotNull(message = "Debe seleccionar un método de pago")
    private EPaymentType paymentType;
}
