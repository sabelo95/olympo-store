package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.domain.services.StockService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class EliminarCarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final StockService stockService;

    public void eliminarCarrito(Long id) {

        Carrito carritoModificar = carritoGateway.obtenerCarritoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El carrito con id " + id + " no existe"));


        stockService.reponerStockDelCarrito(carritoModificar);

        carritoGateway.eliminarCarrito(id);
    }


}