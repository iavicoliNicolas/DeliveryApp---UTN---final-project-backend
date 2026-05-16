package com.deliveryapp.backend.common.services;

import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService {

    private final UserService userService;

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
}
