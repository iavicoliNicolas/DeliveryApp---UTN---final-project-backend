package com.deliveryapp.backend.user.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String role;
}