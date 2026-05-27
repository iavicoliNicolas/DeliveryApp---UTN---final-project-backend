package com.deliveryapp.backend.order.dto;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequestDTO {

    @NotNull(message = "El estado del pedido no puede ser nulo")
    private EOrderStatus status;

}