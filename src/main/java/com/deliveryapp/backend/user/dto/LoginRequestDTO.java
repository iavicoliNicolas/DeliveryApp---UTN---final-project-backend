package com.deliveryapp.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 40, message = "El email no puede superar los 40 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 20, min = 6, message = "La contraseña Debe estar entre 6 y 20 caracteres")
    private String password;

}
