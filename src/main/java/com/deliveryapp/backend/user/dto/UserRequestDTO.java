package com.deliveryapp.backend.user.dto;

import com.deliveryapp.backend.user.enums.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede superar los 20 caracteres")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 30, message = "El apellido no puede superar los 30 caracteres")
    private String lastName;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 40, message = "El email no puede superar los 40 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 20, message = "La contraseña no puede superar los 20 caracteres")
    private String password;

    @NotNull(message = "El rol no puede ser nulo")
    private ERole role;
}
