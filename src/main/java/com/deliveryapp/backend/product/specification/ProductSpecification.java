package com.deliveryapp.backend.product.specification;

import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

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

}

