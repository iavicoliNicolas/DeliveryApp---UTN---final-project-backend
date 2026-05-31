package com.deliveryapp.backend.order.controller;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderLocationRequestDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderStatusRequestDTO;
import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.order.filter.OrderFilter;
import com.deliveryapp.backend.order.service.IOrderService;
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

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAllOrders() {
        log.info("Getting all orders");
        List<OrderResponseDTO> orderResponseDTOList = orderService.findAll();
        log.info("Found {} orders", orderResponseDTOList.size());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable Long id) {
        log.info("Getting order with id {}", id);
        OrderResponseDTO orderResponseDTO = orderService.findById(id).orElseThrow(
                () -> new OrderNotFoundException(id)
        );
        log.info("Found order with id {}", id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO orderRequestDTO) {
        log.info("Creating new order");
        OrderResponseDTO orderResponseDTO = orderService.save(orderRequestDTO);
        log.info("Created new order with id {}", orderResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(orderResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@Valid @PathVariable Long id, @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        log.info("Updating order with id {}", id);
        OrderResponseDTO orderResponseDTO = orderService.update(id, createOrderRequestDTO);
        log.info("Updated order with id {}", orderResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateMerchantOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO dto
    ) {
        log.info("Updating order id {} with status {}", id, dto.getStatus());
        OrderResponseDTO orderResponseDTO = orderService.updateMerchantOrderStatus(id, dto);
        log.info("Updated order id {} with status {}", orderResponseDTO.getId(), orderResponseDTO.getStatus());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTO);
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<OrderResponseDTO> updateOrderLocation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderLocationRequestDTO dto
    ) {
        log.info("Updating order id {} with new location", id);
        OrderResponseDTO orderResponseDTO = orderService.updateOrderLocation(id, dto);
        log.info("Updated order id {} with new location", orderResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order id {}", id);
        orderService.deleteById(id);
        log.info("Deleted order id {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<PaginationResult<OrderResponseDTO>> findOrdersByStore(@PathVariable Long storeId,
                                                                                @ModelAttribute PaginationQuery paginationQuery,
                                                                                @ModelAttribute OrderFilter orderFilter) {
        log.info("Getting orders for store {}", storeId);
        PaginationResult<OrderResponseDTO> paginationResult = orderService.findByStoreId(storeId, paginationQuery, orderFilter);
        log.info("Found {} orders for store {}", paginationResult.getTotalElements(), storeId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(paginationResult);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<OrderResponseDTO>> findUnassignedOrders() {
        log.info("Getting orders unassigned");
        List<OrderResponseDTO> orderResponseDTOList = orderService.findUnassignedOrders();
        log.info("Found {} orders unassigned", orderResponseDTOList.size());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTOList);
    }

}
