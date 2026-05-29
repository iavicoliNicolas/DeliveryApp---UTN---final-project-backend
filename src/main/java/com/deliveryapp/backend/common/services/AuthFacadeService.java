package com.deliveryapp.backend.common.services;

import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthFacadeService {
    String getCurrentUserEmail();

    User getCurrentUser();

    Boolean isRole(ERole role);

    UserDetails authenticate(String username, String password);
}
