package com.deliveryapp.backend.common.data;

import com.deliveryapp.backend.order.enums.EOrderStatus;
import com.deliveryapp.backend.order.enums.EPaymentType;
import com.deliveryapp.backend.order.model.Order;
import com.deliveryapp.backend.order.repository.OrderRepository;
import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import static io.micrometer.common.util.StringUtils.truncate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker(new Locale("es"));

        userRepository.deleteAll();
        storeRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();

        if (userRepository.count() == 0) {
            userRepository.save(
                    User.builder()
                            .name("Juan")
                            .lastName("Skywalker")
                            .email("juanSky@mail.com")
                            .password(passwordEncoder.encode("passDeJuan"))
                            .role(ERole.ROLE_CONSUMER)
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .name("Pedro")
                            .lastName("Nightwalker")
                            .email("pedronight@mail.com")
                            .password(passwordEncoder.encode("passDePedro"))
                            .role(ERole.ROLE_MERCHANT)
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .name("Monica")
                            .lastName("Azul")
                            .email("monicaazul@mail.com")
                            .password(passwordEncoder.encode("passDeMonica"))
                            .role(ERole.ROLE_MERCHANT)
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .name("Ana")
                            .lastName("Sunwalker")
                            .email("ana@mail.com")
                            .password(passwordEncoder.encode("passDeAna"))
                            .role(ERole.ROLE_RIDER)
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .name("Maria")
                            .lastName("Moonwalker")
                            .email("maria@mail.com")
                            .password(passwordEncoder.encode("passDeMaria"))
                            .role(ERole.ROLE_ADMINISTRATOR)
                            .build()
            );


            storeRepository.save(
                    Store.builder()
                            .name("Pizza Hut de Pedro")
                            .address("Calle 13")
                            .owner(userRepository.findByEmail("pedronight@mail.com").get())
                            .latitude(BigDecimal.valueOf(-37.994044345914496))
                            .longitude(BigDecimal.valueOf(-57.55520222953616))
                            .build()
            );

            List<Store> stores = IntStream.rangeClosed(1,20)
                    .mapToObj(i -> Store.builder()
                            .name(truncate(faker.company().name(), 25))
                            .address(truncate(faker.address().streetAddress(), 40))
                                    .latitude(BigDecimal.valueOf(faker.number().randomDouble(7, -38, -37))
                                            .setScale(7, RoundingMode.HALF_UP))
                                    .longitude(BigDecimal.valueOf(faker.number().randomDouble(7, -58, -57))
                                            .setScale(7, RoundingMode.HALF_UP))
                            .owner(userRepository.findByEmail("pedronight@mail.com").get())
                            .build()
                    ).toList();

            storeRepository.saveAll(stores);

            List<Product> products = IntStream.rangeClosed(1, 100)
                    .mapToObj(i -> Product.builder()
                            .name(faker.food().dish())
                            .price(BigDecimal.valueOf(faker.number().numberBetween(1, 100)))
                            .description(faker.lorem().maxLengthSentence(80))
                            .imageURL(faker.internet().image())
                            .status(EProductStatus.values()[faker.random().nextInt(EProductStatus.values().length)])
                            .store(storeRepository.findById(1L).get())
                            .build()
                    ).toList();

            productRepository.saveAll(products);

            List<Product> pizzaProducts = productRepository.findAll()
                    .stream()
                    .filter(product -> product.getStore().getId().equals(1L))
                    .limit(3)
                    .toList();

            User consumer = userRepository.findByEmail("juanSky@mail.com").get();

            User rider = userRepository.findByEmail("ana@mail.com").get();

            Store pizzaStore = storeRepository.findById(1L).get();

            orderRepository.save(
                    Order.builder()
                            .consumer(consumer)
                            .rider(null)
                            .store(pizzaStore)
                            .products(List.of(
                                    pizzaProducts.get(0),
                                    pizzaProducts.get(1)
                            ))
                            .customerAddress("Av Colon 1234")
                            .customerLatitude(BigDecimal.valueOf(-37.9970000))
                            .customerLongitude(BigDecimal.valueOf(-57.5500000))
                            .longitude(pizzaStore.getLongitude())
                            .latitude(pizzaStore.getLatitude())
                            .total(
                                    pizzaProducts.get(0).getPrice()
                                            .add(pizzaProducts.get(1).getPrice())
                            )
                            .status(EOrderStatus.PENDING)
                            .paymentType(EPaymentType.EFECTIVO)
                            .build()
            );

            orderRepository.save(
                    Order.builder()
                            .consumer(consumer)
                            .rider(rider)
                            .store(pizzaStore)
                            .products(List.of(
                                    pizzaProducts.get(1),
                                    pizzaProducts.get(2)
                            ))
                            .customerAddress("Av Independencia 4321")
                            .customerLatitude(BigDecimal.valueOf(-37.9911111))
                            .customerLongitude(BigDecimal.valueOf(-57.5611111))
                            .longitude(pizzaStore.getLongitude())
                            .latitude(pizzaStore.getLatitude())
                            .total(
                                    pizzaProducts.get(1).getPrice()
                                            .add(pizzaProducts.get(2).getPrice())
                            )
                            .status(EOrderStatus.CONFIRMED)
                            .paymentType(EPaymentType.TRANSFERENCIA)
                            .build()
            );

            orderRepository.save(
                    Order.builder()
                            .consumer(consumer)
                            .rider(rider)
                            .store(pizzaStore)
                            .products(List.of(
                                    pizzaProducts.get(0)
                            ))
                            .customerAddress("Calle Falsa 123")
                            .longitude(pizzaStore.getLongitude())
                            .latitude(pizzaStore.getLatitude())
                            .customerLatitude(BigDecimal.valueOf(-37.9855555))
                            .customerLongitude(BigDecimal.valueOf(-57.5755555))
                            .total(
                                    pizzaProducts.get(0).getPrice()
                            )
                            .status(EOrderStatus.COMPLETED)
                            .paymentType(EPaymentType.EFECTIVO)
                            .build()
            );

            orderRepository.save(
                    Order.builder()
                            .consumer(consumer)
                            .rider(null)
                            .store(pizzaStore)
                            .products(List.of(
                                    pizzaProducts.get(2)
                            ))
                            .customerAddress("Diagonal Pueyrredon 555")
                            .longitude(pizzaStore.getLongitude())
                            .latitude(pizzaStore.getLatitude())
                            .customerLatitude(BigDecimal.valueOf(-37.9888888))
                            .customerLongitude(BigDecimal.valueOf(-57.5666666))
                            .total(
                                    pizzaProducts.get(2).getPrice()
                            )
                            .status(EOrderStatus.CANCELLED)
                            .paymentType(EPaymentType.TRANSFERENCIA)
                            .build()
            );

            productRepository.save(
                    Product.builder()
                            .name("Napolitana")
                            .price(BigDecimal.valueOf(10.5))
                            .description("Pizza Napo con todo")
                            .imageURL("/pizzaNapolitana")
                            .status(EProductStatus.AVAILABLE)
                            .store(storeRepository.findById(1L).get())
                            .build()
            );

            productRepository.save(
                    Product.builder()
                            .name("Calabrese")
                            .price(BigDecimal.valueOf(14.0))
                            .description("Pizza Calabria con todo")
                            .imageURL("/pizzaCalabrese")
                            .status(EProductStatus.AVAILABLE)
                            .store(storeRepository.findById(1L).get())
                            .build()
            );

            storeRepository.save(
                    Store.builder()
                            .name("Lomitos Hut de Monica")
                            .address("Calle 20")
                            .owner(userRepository.findByEmail("monicaazul@mail.com").get())
                            .latitude(BigDecimal.valueOf(-37.982457540007395))
                            .longitude(BigDecimal.valueOf(-57.56733097751799))
                            .build()
            );

            products = IntStream.rangeClosed(1, 50)
                    .mapToObj(i -> Product.builder()
                            .name(faker.food().dish())
                            .price(BigDecimal.valueOf(faker.number().numberBetween(1, 100)))
                            .description(faker.lorem().maxLengthSentence(80))
                            .imageURL(faker.internet().image())
                            .status(EProductStatus.values()[faker.random().nextInt(EProductStatus.values().length)])
                            .store(storeRepository.findById(2L).get())
                            .build()
                    ).toList();

            productRepository.saveAll(products);


            productRepository.save(
                    Product.builder()
                            .name("Lomo completo")
                            .price(BigDecimal.valueOf(12.5))
                            .description("Lomo complete con todo")
                            .imageURL("/lomoCompleto")
                            .status(EProductStatus.AVAILABLE)
                            .store(storeRepository.findById(2L).get())
                            .build()
            );

            productRepository.save(
                    Product.builder()
                            .name("Lomo basico")
                            .price(BigDecimal.valueOf(5.5))
                            .description("Lomo solo")
                            .imageURL("/lomo basico")
                            .status(EProductStatus.AVAILABLE)
                            .store(storeRepository.findById(2L).get())
                            .build()
            );


            System.out.println("Datos de prueba cargados");
        }


    }
}
