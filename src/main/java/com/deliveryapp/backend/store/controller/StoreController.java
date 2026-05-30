package com.deliveryapp.backend.store.controller;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.filter.StoreFilter;
import com.deliveryapp.backend.store.service.IStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final IStoreService storeService;

    @GetMapping
    public ResponseEntity<PaginationResult<StoreResponseDTO>> findAllStores(
            @ModelAttribute PaginationQuery paginationQuery,
            @ModelAttribute StoreFilter storeFilter
    ) {
        log.info("Getting all stores");
        PaginationResult<StoreResponseDTO> paginationResult = storeService.findAll(paginationQuery, storeFilter);
        log.info("Found {} stores", paginationResult.getTotalElements());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(paginationResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> findStoreById(@PathVariable Long id) {
        log.info("Getting store by id: {}", id);
        StoreResponseDTO storeResponseDTO = storeService.findById(id).orElseThrow(
                () -> new StoreNotFoundException(id)
        );
        log.info("Found store by id: {}", storeResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeResponseDTO);
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO storeRequestDTO) {
        log.info("Creating new store");
        StoreResponseDTO storeResponseDTO = storeService.save(storeRequestDTO);
        log.info("Created store with id {}", storeResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(storeResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> updateStore(@Valid @PathVariable Long id, @RequestBody StoreRequestDTO storeRequestDTO) {
        log.info("Updating store with id {}", id);
        StoreResponseDTO storeResponseDTO = storeService.update(id, storeRequestDTO);
        log.info("Updated store with id {}", storeResponseDTO.getId());
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.info("Deleting store with id {}", id);
        storeService.deleteById(id);
        log.info("Deleted store with id {}", id);
        return ResponseEntity.noContent().build();
    }

}
