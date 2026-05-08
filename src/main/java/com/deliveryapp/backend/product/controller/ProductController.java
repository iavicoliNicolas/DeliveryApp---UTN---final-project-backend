package com.deliveryapp.backend.product.controller;

import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.service.IProductService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id){
        ProductResponseDTO productResponseDTO = productService.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(productService.save(productRequestDTO));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@Valid @PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productService.update(id,productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Product with id " + id + " deleted");
    }



}

