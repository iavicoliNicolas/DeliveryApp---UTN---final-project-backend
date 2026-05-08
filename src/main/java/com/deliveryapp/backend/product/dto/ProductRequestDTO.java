package com.deliveryapp.backend.product.dto;

import com.deliveryapp.backend.product.enums.EProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(max = 25, message = "El nombre del producto no puede superar los 25 caracteres")
    private String name;

    @NotNull(message = "El precio del producto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 6, fraction = 2, message = "El precio debe tener hasta 2 decimales")
    private BigDecimal price;

    @NotBlank(message = "La descripción del producto no puede estar vacía")
    @Size(max = 50, message = "La descripción no puede superar los 50 caracteres")
    private String description;

    @Size(max = 250, message = "La URL de la imagen no puede superar los 250 caracteres")
    private String imageURL;

    @NotNull(message = "El estado no puede ser nulo")
    private EProductStatus status;

    @NotNull(message = "El id del comercio no puede ser nulo")
    private Long storeId;
}
