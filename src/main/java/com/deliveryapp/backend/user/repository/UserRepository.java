package com.deliveryapp.backend.user.repository;

import com.deliveryapp.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
