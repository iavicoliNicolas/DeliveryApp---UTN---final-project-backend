package com.deliveryapp.backend.user.mapper;

import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public User toEntity(UserRequestDTO dto) {

        ERole role = dto.getRole() != null ? dto.getRole() : ERole.ROLE_CONSUMER;

        return User.builder()

                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(role)
                .build();
    }

    public UserRequestDTO toRequest(User user){

        return UserRequestDTO.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
