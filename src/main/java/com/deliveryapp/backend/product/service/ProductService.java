package com.deliveryapp.backend.product.service;

import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.mapper.ProductMapper;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponse).toList();
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {

        //ver esto de cambir al service de store?
        Store existingStore =  storeRepository.findById(productRequestDTO.getStoreId())
                        .orElseThrow(
                                () -> new RuntimeException("Store not found")
                        );

        // falta verificar que el usuario tipo comerciante sea dueño del store

        Product productSaved = productRepository.save(ProductMapper.toEntity(productRequestDTO, existingStore));

        return ProductMapper.toResponse(productSaved);
    }

    @Override
    public Optional<ProductResponseDTO> findById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toResponse);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );


        Store existingStore =  storeRepository.findById(productRequestDTO.getStoreId())
                .orElseThrow(
                        () -> new StoreNotFoundException(productRequestDTO.getStoreId())
                );

        // falta verificar que el usuario tipo comerciante sea dueño del store

        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setImageURL(productRequestDTO.getImageURL());
        existingProduct.setStatus(productRequestDTO.getStatus());
        existingProduct.setStore(existingStore);

        Product productSaved = productRepository.save(existingProduct);

        return ProductMapper.toResponse(productSaved);
    }

    @Override
    public void deleteById(Long id) {

        //reemplazar por metodo exists? hay que ponerlo en la interface?

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );

        productRepository.deleteById(id);

    }
}
