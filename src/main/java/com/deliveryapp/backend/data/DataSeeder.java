package com.deliveryapp.backend.data;

import com.deliveryapp.backend.store.model.Store;
import com.deliveryapp.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final StoreRepository storeRepository;

    @Override
    public void run(String... args) throws Exception {

        if(storeRepository.count() == 0){
            Store store = new Store();
            store.setName("Store Prueba");
            store.setAddress("Calle 2");
            storeRepository.save(store);

            System.out.println("Datos de prueba cargados");
        }


    }
}
