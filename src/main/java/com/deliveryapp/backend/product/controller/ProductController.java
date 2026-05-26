package com.deliveryapp.backend.product.controller;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.filter.ProductFilter;
import com.deliveryapp.backend.product.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;


    @GetMapping
    public ResponseEntity<PaginationResult<ProductResponseDTO>> findAllProducts(

            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute ProductFilter productFilter
    ) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productService.findAll(paginationQuery, productFilter));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productResponseDTO);
    }

    //Como Comerciante quiero realizar Alta de productos de mi comercio
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(productService.save(productRequestDTO));

    }


    //Como Comerciante quiero realizar Modificacion de productos de mi comercio
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productService.update(id, productRequestDTO));
    }


    //Como Comerciante quiero realizar Baja de productos de mi comercio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

