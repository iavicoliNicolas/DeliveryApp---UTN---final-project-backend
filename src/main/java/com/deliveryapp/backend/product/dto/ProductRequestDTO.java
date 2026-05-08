package com.deliveryapp.backend.product.dto;

import com.deliveryapp.backend.product.enums.EProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    @NotBlank(message = "El nombre del producto no puede estar vacio")
    private String name;
    @NotNull(message = "El precio del producto no puede ser nulo")
    private BigDecimal price;
    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    private String description;
    private String imageURL;
    private EProductStatus status;
    @NotNull(message = "El id del comercio no puede ser nulo")
    private Long storeId;
}
