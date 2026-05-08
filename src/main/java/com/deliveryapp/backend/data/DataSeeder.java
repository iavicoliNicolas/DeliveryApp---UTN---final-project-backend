package com.deliveryapp.backend.data;

import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import com.deliveryapp.backend.user.enums.ERole;
import com.deliveryapp.backend.user.model.User;
import com.deliveryapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        storeRepository.deleteAll();



        if(userRepository.count() == 0){
            User user = new User();
            user.setName("User Prueba");
            user.setLastName("Prueba");
            user.setEmail("emailprueba");
            user.setPassword("12345");
            user.setRole(ERole.MERCHANT);
            userRepository.save(user);

            System.out.println("Datos de prueba cargados");
        }

        if(storeRepository.count() == 0){
            Store store = new Store();
            store.setName("Store Prueba");
            store.setAddress("Calle 2");
            store.setOwner(userRepository.findById(1L).get());
            storeRepository.save(store);

            System.out.println("Datos de prueba cargados");
        }


    }
}
