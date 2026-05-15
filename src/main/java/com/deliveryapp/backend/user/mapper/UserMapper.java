package com.deliveryapp.backend.user.mapper;

import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.model.User;

public class UserMapper {

    public static UserResponseDTO toResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
