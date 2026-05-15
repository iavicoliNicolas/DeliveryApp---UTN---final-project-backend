package com.deliveryapp.backend.user.dto;

import com.deliveryapp.backend.user.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private ERole role;
}
