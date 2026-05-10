package com.deliveryapp.backend.order.service;

import com.deliveryapp.backend.order.dto.OrderRequestDTO;
import com.deliveryapp.backend.order.dto.OrderResponseDTO;
import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.order.mapper.OrderMapper;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.order.repository.OrderRepository;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderResponseDTO> findAll() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponseDTO save(OrderRequestDTO dto) {

        User consumer = new User();
        consumer.setId(1L);

        List<Product> products =
                productRepository.findAllById(dto.getProducts());

        Store store = products.getFirst().getStore();

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = OrderMapper.toEntity(
                dto,
                consumer,
                store,
                total,
                products
        );

        Order newOrder = orderRepository.save(order);

        return OrderMapper.toResponse(newOrder);
    }


    @Override
    public Optional<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id).map(OrderMapper::toResponse);
    }

    @Override
    public OrderResponseDTO update(Long id, OrderRequestDTO dto) {

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

        Store store = products.get(0).getStore();

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setProducts(products);

        order.setStore(store);

        order.setStoreAddress(store.getAddress());

        order.setOrderAddress(dto.getOrderAddress());

        order.setTotal(total);

        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(updatedOrder);
    }

    @Override
    public void deleteById(Long id) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(
                        () -> new OrderNotFoundException(id)
                );

        orderRepository.deleteById(id);
    }
}
