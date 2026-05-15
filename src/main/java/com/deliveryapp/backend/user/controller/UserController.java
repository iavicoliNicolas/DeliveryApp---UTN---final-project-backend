package com.deliveryapp.backend.user.controller;

import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import com.deliveryapp.backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(
            @RequestParam(required = false) ERole role) {
        if (role != null) {
            return ResponseEntity.ok(userService.findAllByRole(role));
        }
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        return ResponseEntity.ok(userService.update(id, userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User with id " + id + " deleted");
    }
}
