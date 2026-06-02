package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.common.pagination.PaginationQuery;
import com.deliveryapp.backend.common.pagination.PaginationResult;
import com.deliveryapp.backend.common.services.AuthFacadeService;
import com.deliveryapp.backend.order.dto.CreateOrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderLocationRequestDTO;
import com.deliveryapp.backend.order.dto.UpdateOrderStatusRequestDTO;
import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.exception.InvalidOrderStatusException;
import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.order.filter.OrderFilter;
import com.deliveryapp.backend.order.mapper.OrderMapper;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.order.repository.OrderRepository;
import com.deliveryapp.backend.order.specification.OrderSpecification;
import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.product.exception.ProductException;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.exception.UserNotAuthorizedException;
import com.deliveryapp.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthFacadeService authFacadeService;
    private final StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginationResult<OrderResponseDTO> findByStoreId(Long storeId, PaginationQuery paginationQuery, OrderFilter orderFilter) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() ->
                        new StoreNotFoundException(storeId)
                );

        if (!store.getOwner().equals(authFacadeService.getCurrentUser())) {
            throw new StoreNotFoundException(storeId);
        }

        if (!(paginationQuery.getDirection().equalsIgnoreCase("asc")
                || paginationQuery.getDirection().equalsIgnoreCase("desc"))) {

            throw new InvalidParameterSortByException(
                    "Parametro direction solo acepta valores: asc , desc"
            );
        }

        if (!(paginationQuery.getSortBy().equalsIgnoreCase("id")
                || paginationQuery.getSortBy().equalsIgnoreCase("total")
                || paginationQuery.getSortBy().equalsIgnoreCase("status")
                || paginationQuery.getSortBy().equalsIgnoreCase("paymentType")
                || paginationQuery.getSortBy().equalsIgnoreCase("lastUpdate"))) {

            throw new InvalidParameterSortByException(
                    "Parametro sortBy solo acepta valores: id , total, status, paymentType, lastUpdate"
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

        Specification<Order> specification = Specification.allOf(
                OrderSpecification.byStoreId(storeId)
                        .and(
                                OrderSpecification.byStatus(orderFilter.getStatus())
                                        .and(
                                                OrderSpecification.byPaymentType(orderFilter.getPaymentType())
                                                        .and(
                                                                OrderSpecification.byConsumerId(orderFilter.getConsumerId())
                                                                        .and(
                                                                                OrderSpecification.byTotal(
                                                                                        orderFilter.getTotalMin(),
                                                                                        orderFilter.getTotalMax()
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );

        Page<Order> page = orderRepository.findAll(specification, pageRequest);

        return new PaginationResult<>(
                page.getContent()
                        .stream()
                        .map(OrderMapper::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public List<OrderResponseDTO> findAll() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponseDTO save(CreateOrderRequestDTO dto) {

        List<Product> products = new ArrayList<>();

        for(Long productId : dto.getProducts()){
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if(optionalProduct.isEmpty()){
                throw new ProductNotFoundException(productId);
            }

            if(optionalProduct.get().getStatus().equals(EProductStatus.UNAVAILABLE)){
                throw new ProductException("El producto " + optionalProduct.get().getName() + " no está disponible");
            }
            optionalProduct.ifPresent(products::add);
        }

        if(dto.getProducts() == null || dto.getProducts().isEmpty()){
            throw new RuntimeException("Debe haber al menos un producto en el pedido");
        }

        Store store = products.getFirst().getStore();

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = OrderMapper.toEntity(
                dto,
                authFacadeService.getCurrentUser(),
                store,
                total,
                products,
                EOrderStatus.PENDING,
                null
        );

        Order newOrder = orderRepository.save(order);

        return OrderMapper.toResponse(newOrder);
    }


    @Override
    public Optional<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id).map(OrderMapper::toResponse);
    }

    @Override
    public OrderResponseDTO update(Long id, CreateOrderRequestDTO dto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id)
                );

        List<Product> products =
                productRepository.findAllById(dto.getProducts());

        if (products.isEmpty()) {
            throw new RuntimeException(
                    "Debe haber al menos un producto"
            );
        }

        Store store = products.getFirst().getStore();

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setProducts(products);

        order.setStore(store);

        order.setCustomerAddress(dto.getCustomerAddress());

        order.setCustomerLatitude(dto.getCustomerLatitude());

        order.setCustomerLongitude(dto.getCustomerLongitude());

        order.setLatitude(dto.getCustomerLatitude());

        order.setCustomerLongitude(dto.getCustomerLongitude());

        order.setTotal(total);

        order.setPaymentType(dto.getPaymentType());

        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponseDTO updateMerchantOrderStatus(
            Long id,
            UpdateOrderStatusRequestDTO dto
    ) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id)
                );

        User currentUser = authFacadeService.getCurrentUser();

        if (!order.getStore().getOwner().equals(currentUser)) {

            throw new StoreNotFoundException(currentUser.getId());
        }

        EOrderStatus currentStatus = order.getStatus();
        EOrderStatus newStatus = dto.getStatus();

        if ((currentStatus == EOrderStatus.PENDING && (newStatus == EOrderStatus.CONFIRMED || newStatus == EOrderStatus.CANCELLED))
        || (currentStatus == EOrderStatus.CONFIRMED_ASSIGNED && newStatus == EOrderStatus.DISPATCHED)) {

            order.setStatus(newStatus);

            Order updatedOrder = orderRepository.save(order);

            return OrderMapper.toResponse(updatedOrder);
        } else {
            throw new InvalidOrderStatusException(
                    "Operación inválida"
            );
        }
    }

    @Override
    public OrderResponseDTO updateRiderOrderStatus(
            Long id,
            UpdateOrderStatusRequestDTO dto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id)
                );

        User currentUser = authFacadeService.getCurrentUser();
        if (!currentUser.getRole().equals(ERole.ROLE_RIDER)) {
            throw new UserNotAuthorizedException("Operación prohibida");
        }

        EOrderStatus currentStatus = order.getStatus();
        EOrderStatus newStatus = dto.getStatus();

        if ((currentStatus == EOrderStatus.CONFIRMED && newStatus == EOrderStatus.CONFIRMED_ASSIGNED)
                || (currentStatus == EOrderStatus.DISPATCHED && newStatus == EOrderStatus.COMPLETED)) {

            order.setStatus(newStatus);
            order.setRider(authFacadeService.getCurrentUser());
            Order updatedOrder = orderRepository.save(order);

            return OrderMapper.toResponse(updatedOrder);
        } else {
            throw new InvalidOrderStatusException(
                    "Operación inválida"
            );
        }

    }

    @Override
    public OrderResponseDTO updateOrderLocation(
            Long id,
            UpdateOrderLocationRequestDTO dto
    ) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id)
                );

        order.setLatitude(dto.getLatitude());

        order.setLongitude(dto.getLongitude());

        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(updatedOrder);
    }

    @Override
    public void deleteById(Long id) {

        orderRepository.findById(id)
                .orElseThrow(
                        () -> new OrderNotFoundException(id)
                );

        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderResponseDTO> findUnassignedOrders() {
        return orderRepository.findByRiderIsNullAndStatus(EOrderStatus.CONFIRMED)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findMyOrders() {
        Long consumerId = authFacadeService.getCurrentUser().getId();
        return orderRepository.findByConsumerId(consumerId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}
