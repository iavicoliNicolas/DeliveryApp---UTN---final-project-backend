package com.deliveryapp.backend.order.model;

import com.deliveryapp.backend.location.model.Location;
import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private User consumer;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "order_address", nullable = false, length = 50)
    private String orderAddress;

    @Column(name = "store_address", nullable = false, length = 50)
    private String storeAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location")
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @Column(name = "total", nullable = false, precision = 6, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status;
}
