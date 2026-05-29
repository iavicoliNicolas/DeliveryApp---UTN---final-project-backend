package com.deliveryapp.backend.user.controller;

import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.common.services.JwtService;
import com.deliveryapp.backend.user.dto.LoginRequestDTO;
import com.deliveryapp.backend.user.dto.RegisterRequestDTO;
import com.deliveryapp.backend.user.dto.TokenResponseDTO;
import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.mapper.UserMapper;
import com.deliveryapp.backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {
    private final IUserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthFacadeService authFacadeService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> registerUser(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        if (userService.existsUserByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("El email ya está utilizado");
        }

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(registerRequestDTO.getEmail());
        userRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        ERole role = registerRequestDTO.getRole() != null ? registerRequestDTO.getRole() : ERole.ROLE_CONSUMER;
        userRequestDTO.setRole(role);
        userRequestDTO.setName(registerRequestDTO.getName());
        userRequestDTO.setLastName(registerRequestDTO.getLastName());

        userService.save(userRequestDTO);

        UserDetails userDetails = authFacadeService.authenticate(registerRequestDTO.getEmail(), registerRequestDTO.getPassword());

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        tokenResponseDTO.setToken(jwtService.generateToken(userDetails));

        return ResponseEntity.ok(tokenResponseDTO);
    }

    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        UserDetails userDetails = authFacadeService.authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        String token = jwtService.generateToken(userDetails);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setToken(token);

        return ResponseEntity.ok(tokenResponseDTO);
    }

}
