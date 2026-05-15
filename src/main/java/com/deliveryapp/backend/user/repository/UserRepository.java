package com.deliveryapp.backend.user.repository;

import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(ERole role);
}
