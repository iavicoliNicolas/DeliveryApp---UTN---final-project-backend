package com.deliveryapp.backend.user.dto;

import com.deliveryapp.backend.user.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private ERole role;
}
