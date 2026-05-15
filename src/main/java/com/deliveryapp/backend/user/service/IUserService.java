package com.deliveryapp.backend.user.service;

import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.enums.ERole;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserResponseDTO> findAll();
    List<UserResponseDTO> findAllByRole(ERole role);
    Optional<UserResponseDTO> findById(Long id);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);
    void deleteById(Long id);
}
