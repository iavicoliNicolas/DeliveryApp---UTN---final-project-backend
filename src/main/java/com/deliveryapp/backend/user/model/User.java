package com.deliveryapp.backend.user.model;

import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 20)
    private String name;

    @Column(name = "user_last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(mappedBy = "rider",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Order> orderRiders = new ArrayList<>();

    @OneToMany(mappedBy = "consumer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Order> orderConsumers = new ArrayList<>();

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Store> stores = new ArrayList<>();

}
