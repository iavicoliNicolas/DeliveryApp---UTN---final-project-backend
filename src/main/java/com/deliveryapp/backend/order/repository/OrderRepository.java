package com.deliveryapp.backend.order.repository;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByStoreId(Long storeId);

    List<Order> findByRiderIsNullAndStatus(EOrderStatus status);

    List<Order> findByConsumerId(Long consumerId);

    Optional<Order> findByRiderIdAndStatusIn(Long riderId, List<EOrderStatus> statuses);
}
