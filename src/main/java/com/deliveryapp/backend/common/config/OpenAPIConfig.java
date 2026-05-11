package com.deliveryapp.backend.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Delivery App API",
                version = "${app.custom.api.version}"
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "Development"
        )
)
@Configuration
public class OpenAPIConfig {
}
