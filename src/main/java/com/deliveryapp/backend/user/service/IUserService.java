package com.deliveryapp.backend.user.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.dto.UserUpdateRequestDTO;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.filter.UserFilter;
import com.deliveryapp.backend.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    @Transactional(readOnly = true)
    PaginationResult<UserResponseDTO> findAll(
            PaginationQuery paginationQuery,
            UserFilter userFilter);

    List<UserResponseDTO> findAllByRole(ERole role);

    Optional<UserResponseDTO> findById(Long id);

    UserResponseDTO save(UserRequestDTO userRequestDTO);

    UserResponseDTO saveMyUserProfile(Long id, UserUpdateRequestDTO userUpdateRequestDTO);

    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);

    void deleteById(Long id);

    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmail(String email);
}
