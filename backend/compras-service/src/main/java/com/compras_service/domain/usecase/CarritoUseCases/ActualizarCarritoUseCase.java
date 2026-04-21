package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.services.StockService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ActualizarCarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final StockService stockService;

    public void modificarCarrito(Long id, Long clienteId, Carrito carritoRequest) {


        Carrito carritoModificar = carritoGateway.obtenerCarritoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El carrito con id " + id + " no existe"));


        if (!carritoModificar.getCliente_id().equals(clienteId)) {
            throw new IllegalArgumentException("El carrito con id " + id + " no pertenece al cliente con id " + clienteId);
        }


        if (carritoRequest.getProductos() == null || carritoRequest.getProductos().isEmpty()) {
            throw new IllegalArgumentException("El carrito debe tener al menos un producto");
        }


        stockService.validarCarritoConAnterior(carritoRequest, carritoModificar);


        stockService.reponerStockDelCarrito(carritoModificar);


        stockService.reducirStockDelCarrito(carritoRequest);


        carritoModificar.getProductos().clear();
        carritoRequest.getProductos().forEach(p -> {
            p.setCarrito(carritoModificar);
            carritoModificar.getProductos().add(p);
        });


        carritoModificar.setFecha_actualizacion(LocalDateTime.now());


        carritoGateway.actualizarCarrito(carritoModificar);
    }
}
