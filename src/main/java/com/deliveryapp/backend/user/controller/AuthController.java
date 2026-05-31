package com.deliveryapp.backend.user.controller;

import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.common.services.JwtService;
import com.deliveryapp.backend.user.dto.*;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.exception.UserEmailAlreadyRegisteredException;
import com.deliveryapp.backend.user.exception.UserNotAuthorizedException;
import com.deliveryapp.backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthFacadeService authFacadeService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("Registering new user with role {}", registerRequestDTO.getRole());

        if (!registerRequestDTO.getRole().equals(ERole.ROLE_RIDER)
           && !registerRequestDTO.getRole().equals(ERole.ROLE_CONSUMER)
           && !registerRequestDTO.getRole().equals(ERole.ROLE_MERCHANT)) {

            throw new UserNotAuthorizedException("Debe ingresar un rol válido");
        }

        if (userService.existsUserByEmail(registerRequestDTO.getEmail())) {
            throw new UserEmailAlreadyRegisteredException("El email " + registerRequestDTO.getEmail() + " ya está utilizado");
        }

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(registerRequestDTO.getEmail());
        userRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        ERole role = registerRequestDTO.getRole() != null ? registerRequestDTO.getRole() : ERole.ROLE_CONSUMER;
        userRequestDTO.setRole(role);
        userRequestDTO.setName(registerRequestDTO.getName());
        userRequestDTO.setLastName(registerRequestDTO.getLastName());

        UserResponseDTO userResponseDTO = userService.save(userRequestDTO);

        UserDetails userDetails = authFacadeService.authenticate(registerRequestDTO.getEmail(), registerRequestDTO.getPassword());

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setToken(jwtService.generateToken(userDetails));
        log.info("New user registered with role {} and id {}", userResponseDTO.getRole(), userResponseDTO.getId());

        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Logging in user");
        UserDetails userDetails = authFacadeService.authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        String token = jwtService.generateToken(userDetails);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setToken(token);
        log.info("User with email {} logged in", loginRequestDTO.getEmail());

        return ResponseEntity.ok(tokenResponseDTO);
    }

}
