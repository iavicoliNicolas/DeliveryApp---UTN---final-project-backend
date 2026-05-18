package com.deliveryapp.backend.order.model;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @Column(name = "total", nullable = false, precision = 9, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status;

    @LastModifiedDate
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    private EPaymentType paymentType;

}
