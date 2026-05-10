package com.deliveryapp.backend.order.controller;

import com.deliveryapp.backend.order.dto.OrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.exception.OrderNotFoundException;
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
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(orderService.save(orderRequestDTO));

    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@Valid @PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(orderService.update(id,orderRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("SOrder with id " + id + " deleted");
    }



}
