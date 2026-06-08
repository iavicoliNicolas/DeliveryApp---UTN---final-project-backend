package com.deliveryapp.backend.store.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.exception.StoreSearchMissingLocationException;
import com.deliveryapp.backend.store.filter.StoreFilter;
import com.deliveryapp.backend.store.mapper.StoreMapper;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.store.specification.StoreSpecification;
import com.deliveryapp.backend.user.enums.ERole;
import lombok.RequiredArgsConstructor;
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
public class StoreService implements IStoreService{
    private final StoreRepository storeRepository;
    private final AuthFacadeService authFacadeService;

    @Override
    @Transactional(readOnly = true)
    public PaginationResult<StoreResponseDTO> findAll(PaginationQuery paginationQuery, StoreFilter storeFilter) {

        if (!(paginationQuery.getDirection().equalsIgnoreCase("asc")
                || paginationQuery.getDirection().equalsIgnoreCase("desc"))) {

            throw new InvalidParameterSortByException(
                    "Parametro direction solo acepta valores: asc , desc"
            );
        }

        if (!(paginationQuery.getSortBy().equalsIgnoreCase("id")
                || paginationQuery.getSortBy().equalsIgnoreCase("name")
                || paginationQuery.getSortBy().equalsIgnoreCase("address"))) {

            throw new InvalidParameterSortByException(
                    "Parametro sortBy solo acepta valores: id , name, address"
            );
        }

        if (authFacadeService.isRole(ERole.ROLE_CONSUMER) &&
                (storeFilter.getLatitude() == null || storeFilter.getLongitude() == null || storeFilter.getDistance() == null)) {

            throw new StoreSearchMissingLocationException(
                    "Falta parametro Latitude, Longitude y Distance"
            );
        }

        PageRequest pageRequest = PageRequest.of(
                paginationQuery.getPage(),
                paginationQuery.getSize(),
                Sort.by(Sort.Direction.fromString(paginationQuery.getDirection()), paginationQuery.getSortBy()
                )
        );

        Specification<Store> specification = Specification.allOf(
                StoreSpecification.byName(storeFilter.getName())
                        .and(StoreSpecification.byDistanceLatitude(storeFilter.getLatitude(), storeFilter.getLongitude(), storeFilter.getDistance())
                                .and(StoreSpecification.byDistanceLongitude(storeFilter.getLatitude(), storeFilter.getLongitude(), storeFilter.getDistance())))
        );

        Page<Store> page = storeRepository.findAll(specification, pageRequest);

        return new PaginationResult<>(
                page.getContent()
                        .stream()
                        .map(StoreMapper::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public List<StoreResponseDTO> findAll() {
        return storeRepository.findAll().stream()
                .map(StoreMapper::toResponse).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDTO save(StoreRequestDTO storeRequestDTO) {

        Store storeSaved = storeRepository.save(StoreMapper.toEntity(storeRequestDTO, authFacadeService.getCurrentUser()));

        return StoreMapper.toResponse(storeSaved);
    }

    @Override
    public Optional<StoreResponseDTO> findById(Long id) {
        return storeRepository.findById(id).map(StoreMapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDTO update(Long id, StoreRequestDTO storeRequestDTO) {

        Store existingStore = storeRepository.findById(id)
                .orElseThrow(
                        () -> new StoreNotFoundException(id)
                );

        if(!existingStore.getOwner().equals(authFacadeService.getCurrentUser())){
            throw new StoreNotFoundException(id);
        }

        existingStore.setName(storeRequestDTO.getName());
        existingStore.setAddress(storeRequestDTO.getAddress());
        existingStore.setLatitude(storeRequestDTO.getLatitude());
        existingStore.setLongitude(storeRequestDTO.getLongitude());

        Store storeSaved = storeRepository.save(existingStore);

        return StoreMapper.toResponse(storeSaved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {

        Store existingStore = storeRepository.findById(id)
                .orElseThrow(
                        () -> new StoreNotFoundException(id)
                );

        if(!existingStore.getOwner().equals(authFacadeService.getCurrentUser()) && !authFacadeService.getCurrentUser().getRole().equals(ERole.ROLE_ADMINISTRATOR)){
            throw new StoreNotFoundException(id);
        }

        storeRepository.deleteById(id);
    }
}
