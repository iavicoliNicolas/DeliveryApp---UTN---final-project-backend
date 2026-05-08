package com.deliveryapp.backend.location.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude", nullable = false, precision = 15, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 15, scale = 2)
    private BigDecimal longitude;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;
}
