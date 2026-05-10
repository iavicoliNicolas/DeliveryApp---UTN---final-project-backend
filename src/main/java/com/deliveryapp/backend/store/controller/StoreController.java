package com.deliveryapp.backend.store.controller;

import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.service.IStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final IStoreService storeService;

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> findAllStores(){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(storeService.findAll());
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
