package com.deliveryapp.backend.order.specification;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import com.deliveryapp.backend.order.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class OrderSpecification {

    public static Specification<Order> byStatus(String status) {

        return (root, criteriaQuery, cb)
                -> {
            if (status == null) {
                return null;
            }

            if (
                    status.trim().equalsIgnoreCase(EOrderStatus.PENDING.toString())
                            || status.trim().equalsIgnoreCase(EOrderStatus.CONFIRMED.toString())
                            || status.trim().equalsIgnoreCase(EOrderStatus.COMPLETED.toString())
                            || status.trim().equalsIgnoreCase(EOrderStatus.CANCELLED.toString())
            ) {

                return cb.equal(
                        cb.lower(root.get("status")),
                        status.trim().toLowerCase()
                );
            }

            return null;
        };
    }

    public static Specification<Order> byPaymentType(String paymentType) {

        return (root, criteriaQuery, cb)
                -> {
            if (paymentType == null) {
                return null;
            }

            if (
                    paymentType.trim().equalsIgnoreCase(EPaymentType.EFECTIVO.toString())
                            || paymentType.trim().equalsIgnoreCase(EPaymentType.TRANSFERENCIA.toString())
            ) {

                return cb.equal(
                        cb.lower(root.get("paymentType")),
                        paymentType.trim().toLowerCase()
                );
            }

            return null;
        };
    }

    public static Specification<Order> byConsumerId(Long consumerId) {

        return (root, criteriaQuery, cb)
                -> consumerId == null
                ? null
                : cb.equal(root.get("consumer").get("id"), consumerId);
    }

    public static Specification<Order> byTotal(BigDecimal totalMin,
                                               BigDecimal totalMax) {

        return (root, criteriaQuery, cb)
                -> {
            if (totalMin == null && totalMax == null) {
                return null;
            } else if (totalMin == null) {

                return cb.lessThanOrEqualTo(
                        root.get("total"),
                        totalMax
                );

            } else if (totalMax == null) {

                return cb.greaterThanOrEqualTo(
                        root.get("total"),
                        totalMin
                );
            }

            return cb.between(
                    root.get("total"),
                    totalMin,
                    totalMax
            );
        };
    }

    public static Specification<Order> byStoreId(Long storeId) {

        return (root, criteriaQuery, cb)
                -> storeId == null
                ? null
                : cb.equal(root.get("store").get("id"), storeId);
    }

}