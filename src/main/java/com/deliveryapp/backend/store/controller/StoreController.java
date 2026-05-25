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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final IStoreService storeService;

    @GetMapping
    public ResponseEntity<PaginationResult<StoreResponseDTO>> findAllStores(

            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude,
            @RequestParam(required = false) Integer distance
    ) {

        PaginationQuery paginationQuery = new PaginationQuery(pageNumber, pageSize, sortBy, direction);

        StoreFilter storeFilter = new StoreFilter(name, latitude, longitude, distance);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeService.findAll(paginationQuery, storeFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> findStoreById(@PathVariable Long id){
        StoreResponseDTO storeResponseDTO = storeService.findById(id).orElseThrow(
                () -> new StoreNotFoundException(id)
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeResponseDTO);
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO storeRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(storeService.save(storeRequestDTO));

    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> updateStore(@Valid @PathVariable Long id, @RequestBody StoreRequestDTO storeRequestDTO){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeService.update(id,storeRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStore(@PathVariable Long id){
        storeService.deleteById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Store with id " + id + " deleted");
    }




}
