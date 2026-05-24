package com.deliveryapp.backend.common.services;

import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;

public interface AuthFacadeService {
    String getCurrentUserEmail();

    User getCurrentUser();

    Boolean isRole(ERole role);
}
