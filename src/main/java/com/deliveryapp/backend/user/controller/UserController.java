package com.deliveryapp.backend.user.controller;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.dto.UserUpdateRequestDTO;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import com.deliveryapp.backend.user.filter.UserFilter;
import com.deliveryapp.backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthFacadeService authFacadeService;

    @GetMapping
    public ResponseEntity<PaginationResult<UserResponseDTO>> findAllUsers(
            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute UserFilter userFilter
    ) {

        log.info("Getting users with filters");

        PaginationResult<UserResponseDTO> paginationResult =
                userService.findAll(paginationQuery, userFilter);

        log.info("Found {} users", paginationResult.getTotalElements());

        return ResponseEntity.ok(paginationResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        log.info("Getting user with id {}", id);
        UserResponseDTO userResponseDTO = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.info("Found user with id {}", id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Creating new user");
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        UserResponseDTO userResponseDTO = userService.save(userRequestDTO);
        log.info("Created new user with id {}", userResponseDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Updating user with id {}", id);
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        UserResponseDTO userResponseDTO = userService.update(id, userRequestDTO);
        log.info("Updated user with id {}", userResponseDTO.getId());
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id {}", id);
        userService.deleteById(id);
        log.info("Deleted user with id {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findMyProfile() {
        Long id = authFacadeService.getCurrentUser().getId();
        log.info("Getting own user profile for user id {}", id);
        UserResponseDTO userResponseDTO = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.info("Found own user profile for user id {}", id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        Long id = authFacadeService.getCurrentUser().getId();
        log.info("Updating own user profile for user id {}", id);
        userUpdateRequestDTO.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getPassword()));
        UserResponseDTO userResponseDTO = userService.saveMyUserProfile(id, userUpdateRequestDTO);
        log.info("Updated own user profile for user id {}", id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile() {
        Long id = authFacadeService.getCurrentUser().getId();
        log.info("Deleting own user profile for user id {}", id);
        userService.deleteById(id);
        log.info("Deleted own user profile for user id {}", id);
        return ResponseEntity.noContent().build();
    }

}
