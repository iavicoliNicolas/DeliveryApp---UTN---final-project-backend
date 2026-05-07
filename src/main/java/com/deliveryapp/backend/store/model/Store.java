package com.deliveryapp.backend.store.model;

import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false, length = 25)
    private String name;

    @Column(name = "store_address", nullable = false, length = 40)
    private String address;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
