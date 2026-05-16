package com.deliveryapp.backend.user.controller;

import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.mapper.UserMapper;
import com.deliveryapp.backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        if(userService.existsUserByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("El email ya está utilizado");
        }

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        var user = userMapper.toEntity(userRequestDTO);
        var savedUser = userService.save(userMapper.toRequest(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserResponseDTO> loginUser(
//            @Valid @RequestBody UserRequestDTO userRequestDTO) {
//
//        if(!userService.existsByEmail(userRequestDTO.getEmail())){
//            throw new RuntimeException("Email incorrecto");
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDTO));
//    }

}
