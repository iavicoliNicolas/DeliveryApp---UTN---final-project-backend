package com.deliveryapp.backend.store.service;

import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.store.dto.StoreRequestDTO;
import com.deliveryapp.backend.store.dto.StoreResponseDTO;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.store.mapper.StoreMapper;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService implements IStoreService{
    private final StoreRepository storeRepository;
    private final AuthFacadeService authFacadeService;

    @Override
    public List<StoreResponseDTO> findAll() {
        return storeRepository.findAll().stream()
                .map(StoreMapper::toResponse).toList();
    }

    @Override
    public StoreResponseDTO save(StoreRequestDTO storeRequestDTO) {

        Store storeSaved = storeRepository.save(StoreMapper.toEntity(storeRequestDTO, authFacadeService.getCurrentUser()));

        return StoreMapper.toResponse(storeSaved);
    }

    @Override
    public Optional<StoreResponseDTO> findById(Long id) {
        return storeRepository.findById(id).map(StoreMapper::toResponse);
    }

    @Override
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
    public void deleteById(Long id) {

        Store existingStore = storeRepository.findById(id)
                .orElseThrow(
                        () -> new StoreNotFoundException(id)
                );

        if(!existingStore.getOwner().equals(authFacadeService.getCurrentUser())){
            throw new StoreNotFoundException(id);
        }

        storeRepository.deleteById(id);
    }
}
