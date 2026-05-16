package com.deliveryapp.backend.common.data;

import com.deliveryapp.backend.product.enums.EProductStatus;
import com.deliveryapp.backend.product.model.Product;
import com.deliveryapp.backend.product.repository.ProductRepository;
import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        storeRepository.deleteAll();

        if(userRepository.count() == 0){
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
                            .name("Pizza Hut")
                            .address("Calle 13")
                            .owner(userRepository.findById(2L).get())
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

            System.out.println("Datos de prueba cargados");
        }



    }
}
