package com.deliveryapp.backend.store.dto;

import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class StoreRequestDTO {

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    @Size(max = 25, message = "El nombre del comercio no puede superar los 25 caracteres")
    private String name;

    @NotBlank(message = "La dirección del comercio no puede estar vacía")
    @Size(max = 40, message = "La dirección no puede superar los 40 caracteres")
    private String address;

    @NotNull(message = "El dueño no puede ser nulo")
    private Long ownerId;

}
