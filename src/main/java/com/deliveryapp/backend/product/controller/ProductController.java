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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<PaginationResult<ProductResponseDTO>> findAllProducts(
            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute ProductFilter productFilter
    ) {
        log.info("Getting all products");
        PaginationResult<ProductResponseDTO> paginationResult = productService.findAll(paginationQuery, productFilter);
        log.info("Found {} products", paginationResult.getTotalElements());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(paginationResult);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id) {
        log.info("Getting product with id {}", id);
        ProductResponseDTO productResponseDTO = productService.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        log.info("Found product with id {},", id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("Creating new product");
        ProductResponseDTO productResponseDTO = productService.save(productRequestDTO);
        log.info("Created new product with id {}", productResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(productResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("Updating product with id {}", id);
        ProductResponseDTO productResponseDTO = productService.update(id, productRequestDTO);
        log.info("Updated product with id {}", id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(productResponseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id {}", id);
        productService.deleteById(id);
        log.info("Deleted product with id {}", id);
        return ResponseEntity.noContent().build();
    }

}

