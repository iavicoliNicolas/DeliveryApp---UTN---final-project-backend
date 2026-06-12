package com.deliveryapp.backend.product.model;

import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.store.model.Store;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 40)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "imageURL", length = 250)
    private String imageURL;

    @Enumerated(EnumType.STRING)
    private EProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToMany(
            mappedBy = "products",
            cascade = CascadeType.ALL)
    private List<Order> accounts =
            new ArrayList<>();


}
