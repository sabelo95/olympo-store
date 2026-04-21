package com.compras_service.config;


import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.services.CarritoAbandonadoService;
import com.compras_service.domain.services.EstadoPedidoService;
import com.compras_service.domain.services.StockService;
import com.compras_service.domain.validators.PedidoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = {
                "com.compras_service.domain.usecase",
                "com.compras_service.infrastructure.adapters.repository",
                "com.compras_service.infrastructure.adapters.mapper",
                "com.compras_service.infrastructure.controllers"
        },
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "^.+UseCase$"
                )
        },
        useDefaultFilters = false

)
public class UseCasesConfig {


    @Bean
    public StockService stockDomainService(ProductoGateway productoGateway) {
        return new StockService(productoGateway);
    }

    @Bean
    public PedidoValidator pedidoValidator() {
        return new PedidoValidator();
    }

    @Bean
    public CarritoAbandonadoService carritoAbandonadoService(){
        return new CarritoAbandonadoService();
    }

    @Bean
    public EstadoPedidoService estadoPedidoService() {
        return new EstadoPedidoService();
    }





}
