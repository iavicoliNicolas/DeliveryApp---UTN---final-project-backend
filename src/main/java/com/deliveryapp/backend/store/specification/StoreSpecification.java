package com.deliveryapp.backend.store.specification;

import com.deliveryapp.backend.store.model.Store;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class StoreSpecification {

    public static Specification<Store> byName(String name) {
        return (root, criteriaQuery, cb)
                -> name == null
                ? null
                : cb.like(
                cb.lower(root.get("name")),
                "%" + (name.isBlank() ? "" : name.toLowerCase()) + "%"
        );
    }

    public static Specification<Store> byDistanceLatitude(BigDecimal latitude, BigDecimal longitude, Integer distance) {

        return (root, criteriaQuery, cb)
                -> {
            if (distance == null || latitude == null || longitude == null) {
                return null;
            }

            BigDecimal latitudeA =
                    latitude.add(BigDecimal.valueOf((double) distance / 111111));

            BigDecimal latitudeB =
                    latitude.subtract(BigDecimal.valueOf((double) distance / 111111));

            return cb.between(
                    root.get("latitude"), latitudeB, latitudeA
            );
        };
    }

    public static Specification<Store> byDistanceLongitude(BigDecimal latitude, BigDecimal longitude, Integer distance) {

        return (root, criteriaQuery, cb)
                -> {
            if (distance == null || latitude == null || longitude == null) {
                return null;
            }

            BigDecimal longitudeA =
                    longitude.add(BigDecimal.valueOf((double) distance / 111111));

            BigDecimal longitudeB =
                    longitude.subtract(BigDecimal.valueOf((double) distance / 111111));

            return cb.between(
                    root.get("longitude"), longitudeB, longitudeA
            );
        };
    }

}