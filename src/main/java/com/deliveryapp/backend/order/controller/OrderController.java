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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;


    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAllOrders(){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable Long id){
        OrderResponseDTO orderResponseDTO = orderService.findById(id).orElseThrow(
                () -> new OrderNotFoundException(id)
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderResponseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO orderRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(orderService.save(orderRequestDTO));

    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@Valid @PathVariable Long id, @RequestBody CreateOrderRequestDTO createOrderRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderService.update(id,createOrderRequestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateMerchantOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO dto
    ) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(orderService.updateMerchantOrderStatus(id, dto));
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<OrderResponseDTO> updateOrderLocation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderLocationRequestDTO dto
    ) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(orderService.updateOrderLocation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Order with id " + id + " deleted");
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<PaginationResult<OrderResponseDTO>> findOrdersByStore(@PathVariable Long storeId,
                                                                                @ModelAttribute PaginationQuery paginationQuery,
                                                                                @ModelAttribute OrderFilter orderFilter) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(orderService.findByStoreId(storeId, paginationQuery, orderFilter));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<OrderResponseDTO>> findUnassignedOrders() {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(orderService.findUnassignedOrders());
    }

}
