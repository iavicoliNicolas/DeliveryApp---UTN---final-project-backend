package com.deliveryapp.backend.common.services;

import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getCurrentUserEmail() {
        return getAuthentication().getName();
    }

    @Override
    public User getCurrentUser() {
        return userService.findUserByEmail(getCurrentUserEmail()).get();
    }

    @Override
    public Boolean isRole(ERole role) {
        Set<String> roles = getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return roles.contains(role.toString());
    }

    @Override
    public UserDetails authenticate(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password //lo hago asi porque necesito las password sin codificar
                )
        );
        /*
        UserDetails userDetails = new org.springframework.security.core.userdetails. User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
*/
        return (UserDetails) authentication.getPrincipal();
    }

}
