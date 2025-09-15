package com.example.FacturadorFXSmart.config;


import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fxSmartOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Facturador FX Smart API")
                        .version("v1")
                        .description("API para facturas")
                        .license(new License().name("MIT")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Local")));
    }
}
