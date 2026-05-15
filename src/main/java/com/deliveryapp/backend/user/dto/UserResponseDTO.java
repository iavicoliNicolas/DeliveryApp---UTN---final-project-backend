package com.deliveryapp.backend.user.dto;

import com.deliveryapp.backend.user.enums.ERole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private ERole role;
}
