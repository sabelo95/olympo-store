package com.compras_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Compras Service API")
                        .version("1.0.0")
                        .description("API RESTful para gestión de compras, pedidos, carritos y clientes. " +
                                "Proporciona endpoints para realizar operaciones CRUD sobre clientes, " +
                                "carritos de compra y pedidos.")
                        .contact(new Contact()
                                .name("Equipo Arka")
                                .email("santiagoberriolopez@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("https://arka-sbl.click")
                                .description("Gateway - Producción (usar este para pruebas desde Swagger UI)"),
                        new Server()
                                .url("http://localhost:8084")
                                .description("Servidor local de desarrollo")
                ))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header usando el esquema Bearer. " +
                                        "Ejemplo: \"Authorization: Bearer {token}\"")));
    }
}

