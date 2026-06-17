package com.deliveryapp.backend.order.specification;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import com.deliveryapp.backend.order.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSpecification {

        public static Specification<Order> byId(Long id) {

            return (root, query, cb)
                    -> id == null
                    ? null
                    : cb.equal(root.get("id"), id);
        }

        public static Specification<Order> byRiderId(Long riderId) {

            return (root, query, cb)
                    -> riderId == null
                    ? null
                    : cb.equal(root.get("rider").get("id"), riderId);
        }

        public static Specification<Order> byConsumerId(Long consumerId) {

            return (root, query, cb)
                    -> consumerId == null
                    ? null
                    : cb.equal(root.get("consumer").get("id"), consumerId);
        }

        public static Specification<Order> byStoreId(Long storeId) {

            return (root, query, cb)
                    -> storeId == null
                    ? null
                    : cb.equal(root.get("store").get("id"), storeId);
        }

        public static Specification<Order> byCustomerAddress(String customerAddress) {

            return (root, query, cb)
                    -> customerAddress == null
                    ? null
                    : cb.like(
                    cb.lower(root.get("customerAddress")),
                    "%" + (customerAddress.isBlank()
                           ? ""
                           : customerAddress.toLowerCase()) + "%"
            );
        }

        public static Specification<Order> byStatus(EOrderStatus status) {

            return (root, query, cb)
                    -> status == null
                    ? null
                    : cb.equal(root.get("status"), status);
        }

        public static Specification<Order> byPaymentType(EPaymentType paymentType) {

            return (root, query, cb)
                    -> paymentType == null
                    ? null
                    : cb.equal(root.get("paymentType"), paymentType);
        }

        public static Specification<Order> byTotal(
                BigDecimal totalMin,
                BigDecimal totalMax
        ) {

            return (root, query, cb)
                    -> {

                if (totalMin == null && totalMax == null) {
                    return null;
                }

                if (totalMin == null) {
                    return cb.lessThanOrEqualTo(
                            root.get("total"),
                            totalMax
                    );
                }

                if (totalMax == null) {
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

        public static Specification<Order> byLastUpdate(
                LocalDateTime lastUpdateFrom,
                LocalDateTime lastUpdateTo
        ) {

            return (root, query, cb)
                    -> {

                if (lastUpdateFrom == null && lastUpdateTo == null) {
                    return null;
                }

                if (lastUpdateFrom == null) {
                    return cb.lessThanOrEqualTo(
                            root.get("lastUpdate"),
                            lastUpdateTo
                    );
                }

                if (lastUpdateTo == null) {
                    return cb.greaterThanOrEqualTo(
                            root.get("lastUpdate"),
                            lastUpdateFrom
                    );
                }

                return cb.between(
                        root.get("lastUpdate"),
                        lastUpdateFrom,
                        lastUpdateTo
                );
            };
        }
    }