package com.deliveryapp.backend.user.specification;

import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> byId(Long id) {

        return (root, criteriaQuery, cb)
                -> id == null
                ? null
                : cb.equal(root.get("id"), id);
    }

    public static Specification<User> byName(String name) {

        return (root, criteriaQuery, cb)
                -> name == null
                ? null
                : cb.like(
                cb.lower(root.get("name")),
                "%" + (name.isBlank() ? "" : name.toLowerCase()) + "%"
        );
    }

    public static Specification<User> byLastName(String lastName) {

        return (root, criteriaQuery, cb)
                -> lastName == null
                ? null
                : cb.like(
                cb.lower(root.get("lastName")),
                "%" + (lastName.isBlank() ? "" : lastName.toLowerCase()) + "%"
        );
    }

    public static Specification<User> byEmail(String email) {

        return (root, criteriaQuery, cb)
                -> email == null
                ? null
                : cb.like(
                cb.lower(root.get("email")),
                "%" + (email.isBlank() ? "" : email.toLowerCase()) + "%"
        );
    }

    public static Specification<User> byRole(String role) {

        return (root, criteriaQuery, cb) -> {

            if (role == null) {
                return null;
            }

            for (ERole value : ERole.values()) {

                if (value.name().equalsIgnoreCase(role.trim())) {

                    return cb.equal(
                            cb.lower(root.get("role")),
                            role.trim().toLowerCase()
                    );
                }
            }

            return null;
        };
    }
}