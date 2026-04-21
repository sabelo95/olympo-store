package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.services.StockService;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CrearCarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final StockService stockService;

    public Carrito crearCarrito(Carrito carrito) {


        stockService.validarCarrito(carrito);


        stockService.reducirStockDelCarrito(carrito);


        return carritoGateway.crearCarrito(carrito);
    }


}
