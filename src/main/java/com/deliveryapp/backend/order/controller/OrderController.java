package com.deliveryapp.backend.order.controller;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderLocationRequestDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderStatusRequestDTO;
import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.order.filter.AdminOrderFilter;
import com.deliveryapp.backend.order.filter.OrderFilter;
import com.deliveryapp.backend.order.service.IOrderService;
import com.deliveryapp.backend.user.enums.ERole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderService orderService;
    private final AuthFacadeService authFacadeService;

    @GetMapping
    public ResponseEntity<PaginationResult<OrderResponseDTO>> findAllOrders(
            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute AdminOrderFilter orderFilter
    ) {

        log.info("Getting all orders with filters {}", orderFilter);

        PaginationResult<OrderResponseDTO> paginationResult =
                orderService.findAll(paginationQuery, orderFilter);

        log.info("Found {} orders",
                paginationResult.getTotalElements());

        return ResponseEntity.ok(paginationResult);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> findMyOrders() {

        log.info("Getting orders for authenticated consumer");

        List<OrderResponseDTO> orderResponseDTOList =
                orderService.findMyOrders();

        log.info("Found {} orders for authenticated consumer",
                orderResponseDTOList.size());

        return ResponseEntity.ok(orderResponseDTOList);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<OrderResponseDTO>> findUnassignedOrders() {

        log.info("Getting unassigned orders");

        List<OrderResponseDTO> orderResponseDTOList =
                orderService.findUnassignedOrders();

        log.info("Found {} unassigned orders",
                orderResponseDTOList.size());

        return ResponseEntity.ok(orderResponseDTOList);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<PaginationResult<OrderResponseDTO>> findOrdersByStore(
            @PathVariable Long storeId,
            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute OrderFilter orderFilter
    ) {

        log.info("Getting orders for store {}", storeId);

        PaginationResult<OrderResponseDTO> paginationResult =
                orderService.findByStoreId(
                        storeId,
                        paginationQuery,
                        orderFilter
                );

        log.info("Found {} orders for store {}",
                paginationResult.getTotalElements(),
                storeId);

        return ResponseEntity.ok(paginationResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderById(
            @PathVariable Long id
    ) {

        log.info("Getting order with id {}", id);

        OrderResponseDTO orderResponseDTO =
                orderService.findById(id)
                        .orElseThrow(() -> new OrderNotFoundException(id));

        log.info("Found order with id {}", id);

        return ResponseEntity.ok(orderResponseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO orderRequestDTO
    ) {

        log.info("Creating new order");

        OrderResponseDTO orderResponseDTO =
                orderService.save(orderRequestDTO);

        log.info("Created order with id {}",
                orderResponseDTO.getId());

        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(orderResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO
    ) {

        log.info("Updating order with id {}", id);

        OrderResponseDTO orderResponseDTO =
                orderService.update(id, createOrderRequestDTO);

        log.info("Updated order with id {}",
                orderResponseDTO.getId());

        return ResponseEntity.ok(orderResponseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO dto
    ) {

        log.info("Updating order {} status to {}",
                id,
                dto.getStatus());

        OrderResponseDTO orderResponseDTO;

        if (authFacadeService.isRole(ERole.ROLE_MERCHANT)) {

            log.info("Authenticated user is MERCHANT");

            orderResponseDTO =
                    orderService.updateMerchantOrderStatus(id, dto);

        } else if (authFacadeService.isRole(ERole.ROLE_RIDER)) {

            log.info("Authenticated user is RIDER");

            orderResponseDTO =
                    orderService.updateRiderOrderStatus(id, dto);

        } else {

            log.warn(
                    "User with role {} attempted to update order status",
                    authFacadeService.getCurrentUser().getRole()
            );

            throw new RuntimeException(
                    "No tiene permisos para modificar estados de pedidos"
            );
        }

        log.info("Order {} updated to status {}",
                orderResponseDTO.getId(),
                orderResponseDTO.getStatus());

        return ResponseEntity.ok(orderResponseDTO);
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<OrderResponseDTO> updateOrderLocation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderLocationRequestDTO dto
    ) {

        log.info("Updating location for order {}", id);

        OrderResponseDTO orderResponseDTO =
                orderService.updateOrderLocation(id, dto);

        log.info("Location updated for order {}",
                orderResponseDTO.getId());

        return ResponseEntity.ok(orderResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id
    ) {

        log.info("Deleting order {}", id);

        orderService.deleteById(id);

        log.info("Deleted order {}", id);

        return ResponseEntity.noContent().build();
    }
}
