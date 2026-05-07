package com.deliveryapp.backend.product.model;

import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.store.model.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 25)
    private String name;

    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @Column(name = "imageURL", length = 250)
    private String imageURL;

    @Enumerated(EnumType.STRING)
    private EProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
