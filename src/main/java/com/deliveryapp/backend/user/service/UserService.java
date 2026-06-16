package com.deliveryapp.backend.user.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.user.dto.UserRequestDTO;
import com.deliveryapp.backend.user.dto.UserResponseDTO;
import com.deliveryapp.backend.user.dto.UserUpdateRequestDTO;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import com.deliveryapp.backend.user.filter.UserFilter;
import com.deliveryapp.backend.user.mapper.UserMapper;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import com.deliveryapp.backend.user.specification.UserSpecification;
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
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    // private final AuthFacadeService authFacadeService;
    //private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public PaginationResult<UserResponseDTO> findAll(
            PaginationQuery paginationQuery,
            UserFilter userFilter) {

        if (!(paginationQuery.getDirection().equalsIgnoreCase("asc")
                || paginationQuery.getDirection().equalsIgnoreCase("desc"))) {

            throw new InvalidParameterSortByException(
                    "Parametro direction solo acepta valores: asc , desc"
            );
        }

        if (!(paginationQuery.getSortBy().equalsIgnoreCase("id")
                || paginationQuery.getSortBy().equalsIgnoreCase("name")
                || paginationQuery.getSortBy().equalsIgnoreCase("lastName")
                || paginationQuery.getSortBy().equalsIgnoreCase("email")
                || paginationQuery.getSortBy().equalsIgnoreCase("role"))) {

            throw new InvalidParameterSortByException(
                    "Parametro sortBy solo acepta valores: id , name, lastName, email, role"
            );
        }

        PageRequest pageRequest = PageRequest.of(
                paginationQuery.getPage(),
                paginationQuery.getSize(),
                Sort.by(
                        Sort.Direction.fromString(
                                paginationQuery.getDirection()
                        ),
                        paginationQuery.getSortBy()
                )
        );

        Specification<User> specification = Specification.allOf(
                UserSpecification.byId(userFilter.getId())
                        .and(UserSpecification.byName(userFilter.getName())
                                .and(UserSpecification.byLastName(userFilter.getLastName())
                                        .and(UserSpecification.byEmail(userFilter.getEmail())
                                                .and(UserSpecification.byRole(userFilter.getRole()))))
                        ));

        Page<User> page = userRepository.findAll(specification, pageRequest);

        return new PaginationResult<>(
                page.getContent()
                        .stream()
                        .map(userMapper::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public List<UserResponseDTO> findAllByRole(ERole role) {
        return userRepository.findAllByRole(role)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toResponse);
    }

    @Override
    public UserResponseDTO save(UserRequestDTO dto) {
        User savedUser = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toResponse(savedUser);
    }

    public UserResponseDTO saveMyUserProfile(Long id, UserUpdateRequestDTO dto) {

        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setName(dto.getName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setPassword(dto.getPassword());


        User savedUser = userRepository.save(existingUser);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setName(dto.getName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(dto.getPassword());
        existingUser.setRole(dto.getRole());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
