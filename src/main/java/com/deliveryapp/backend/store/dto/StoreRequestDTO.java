package com.deliveryapp.backend.store.dto;

import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDTO {

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    @Size(max = 25, message = "El nombre del comercio no puede superar los 25 caracteres")
    private String name;

    @NotBlank(message = "La dirección del comercio no puede estar vacía")
    @Size(max = 40, message = "La dirección no puede superar los 40 caracteres")
    private String address;

    @NotNull(message = "La latitud del comercio no puede estar vacía")
    private BigDecimal latitude;

    @NotNull(message = "La longitud del comercio no puede estar vacía")
    private BigDecimal longitude;

}
