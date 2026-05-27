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
    private String customerAddress;

    @NotNull(message = "Debe haber mínimo un producto")
    private List<Long> products;

    @NotNull(message = "La latitud de la entrega no puede ser nula")
    private BigDecimal customerLatitude;

    @NotNull(message = "La longitud de la entrega no puede ser nula")
    private BigDecimal customerLongitude;

    @NotNull(message = "Debe seleccionar un método de pago")
    private EPaymentType paymentType;
}
