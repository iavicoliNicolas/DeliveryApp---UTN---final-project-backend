package com.deliveryapp.backend.product.specification;

import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> byName(String name) {
        return (root, criteriaQuery, cb)
                -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + (name.isBlank() ? "" : name.toLowerCase()) + "%");
    }

    public static Specification<Product> byDescription(String description) {
        return (root, criteriaQuery, cb)
                -> description == null ? null : cb.like(cb.lower(root.get("description")), "%" + (description.isBlank() ? "" : description.toLowerCase()) + "%");
    }

    public static Specification<Product> byStatus(String status) {
        return (root, criteriaQuery, cb)
                -> {
            if (status == null) {
                return null;
            }
            if (status.trim().equalsIgnoreCase(EProductStatus.AVAILABLE.toString())
                    || status.trim().equalsIgnoreCase(EProductStatus.UNAVAILABLE.toString())) {
                return cb.equal(cb.lower(root.get("status")), status.trim().toLowerCase());
            } else {
                return null;
            }

        };
    }

    public static Specification<Product> byStoreId(Long storeId) {
        return (root, criteriaQuery, cb)
                -> storeId == null ? null : cb.equal(root.get("store").get("id"), storeId);
    }

    public static Specification<Product> byPrice(BigDecimal priceMin, BigDecimal priceMax) {
        return (root, criteriaQuery, cb)
                -> {
            if (priceMin == null && priceMax == null) {
                return null;
            } else if (priceMin == null) {
                return cb.lessThanOrEqualTo(root.get("price"), priceMax);
            } else if (priceMax == null) {
                return cb.greaterThanOrEqualTo(root.get("price"), priceMin);
            }

            return cb.between(root.get("price"), priceMin, priceMax);
        };
    }

    public static Specification<Product> byDistanceLatitude(BigDecimal latitude, BigDecimal longitude, Integer distance) {
        return (root, criteriaQuery, cb)
                -> {
            if (distance == null || latitude == null || longitude == null) {
                return null;
            }
            BigDecimal latitudeA = latitude.add(BigDecimal.valueOf((double) distance / 111));
            BigDecimal latitudeB = latitude.subtract(BigDecimal.valueOf((double) distance / 111));
            return cb.between(root.get("store").get("latitude"),
                    latitudeB, latitudeA);
        };

    }

    public static Specification<Product> byDistanceLongitude(BigDecimal latitude, BigDecimal longitude, Integer distance) {
        return (root, criteriaQuery, cb)
                -> {
            if (distance == null || latitude == null || longitude == null) {
                return null;
            }
            BigDecimal longitudeA = longitude.add(BigDecimal.valueOf((double) distance / 111));
            BigDecimal longitudeB = longitude.subtract(BigDecimal.valueOf((double) distance / 111));

            return cb.between(root.get("store").get("longitude"),
                    longitudeB, longitudeA);
        };
    }

}

