package com.deliveryapp.backend.product.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.product.dto.ProductRequestDTO;
import com.deliveryapp.backend.product.dto.ProductResponseDTO;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.exception.ProductSearchMissingLocationException;
import com.deliveryapp.backend.product.filter.ProductFilter;
import com.deliveryapp.backend.product.mapper.ProductMapper;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.product.specification.ProductSpecification;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.user.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final AuthFacadeService authFacadeService;

    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResult<ProductResponseDTO> findAll(PaginationQuery paginationQuery, ProductFilter productFilter) {

        if (!(paginationQuery.getDirection().equalsIgnoreCase("asc") || paginationQuery.getDirection().equalsIgnoreCase("desc"))) {
            throw new InvalidParameterSortByException("Parametro direction solo acepta valores: asc , desc");
        }
        if (!(paginationQuery.getSortBy().equalsIgnoreCase("id") || paginationQuery.getSortBy().equalsIgnoreCase("name")
                || paginationQuery.getSortBy().equalsIgnoreCase("price") || paginationQuery.getSortBy().equalsIgnoreCase("status")
        )) {
            throw new InvalidParameterSortByException("Parametro sortBy solo acepta valores: id , name, price, status");
        }

        if (authFacadeService.isRole(ERole.ROLE_CONSUMER) &&
                (productFilter.getLatitude() == null || productFilter.getLongitude() == null || productFilter.getDistance() == null)) {
            throw new ProductSearchMissingLocationException("Falta parametro Latitude, Longitude y Distance");
        }

        PageRequest pageRequest = PageRequest.of(
                paginationQuery.getPage(),
                paginationQuery.getSize(),
                Sort.by(Sort.Direction.fromString(paginationQuery.getDirection()), paginationQuery.getSortBy())
        );

        Specification<Product> specification = Specification.allOf(
                ProductSpecification.byName(productFilter.getName())
                        .and(ProductSpecification.byDescription(productFilter.getDescription())
                                .and(ProductSpecification.byStatus(productFilter.getStatus())
                                        .and(ProductSpecification.byStoreId(productFilter.getStoreId())
                                                .and(ProductSpecification.byPrice(productFilter.getPriceMin(), productFilter.getPriceMax())
                                                        .and(ProductSpecification.byDistanceLatitude(productFilter.getLatitude(), productFilter.getLongitude(), productFilter.getDistance())
                                                                .and(ProductSpecification.byDistanceLongitude(productFilter.getLatitude(), productFilter.getLongitude(), productFilter.getDistance())))))))
        );

        Page<Product> page = productRepository.findAll(specification, pageRequest);

        return new PaginationResult<>(
                page.getContent().stream().map(ProductMapper::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {
        Store existingStore = getExistingStore(productRequestDTO);
        isOwnerVerification(existingStore);
        Product productSaved = productRepository.save(ProductMapper.toEntity(productRequestDTO, existingStore));

        return ProductMapper.toResponse(productSaved);
    }

    @Override
    public Optional<ProductResponseDTO> findById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );
        //verifico que el comerciante sea dueño del store del producto
        isOwnerVerification(existingProduct.getStore());

        //verifco que exista el store nuevo en caso de cambio
        Store existingStore = getExistingStore(productRequestDTO);

        //verifico que el usuario tipo comerciante sea dueño del store
        isOwnerVerification(existingStore);

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
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );

        //verifico que el comerciante sea dueño del store del producto que quiere borrar
        isOwnerVerification(existingProduct.getStore());

        productRepository.deleteById(id);

    }


    private void isOwnerVerification(Store existingStore) {
        if (!existingStore.getOwner().equals(authFacadeService.getCurrentUser())) {
            throw new StoreNotFoundException(existingStore.getId());
        }
    }

    private @NonNull Store getExistingStore(ProductRequestDTO productRequestDTO) {
        Store existingStore = storeRepository.findById(productRequestDTO.getStoreId())
                .orElseThrow(
                        () -> new StoreNotFoundException(productRequestDTO.getStoreId())
                );
        return existingStore;
    }

}
