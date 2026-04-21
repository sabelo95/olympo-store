package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.model.Carrito;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class ObtenerCarritoUseCase {

    private final CarritoGateway carritoGateway;

    public Optional<Carrito> obtenerCarritoActivo(Long clienteId) {
        LocalDateTime limite = LocalDateTime.now().minusHours(2);
        return carritoGateway.obtenerCarritoActivoPorCliente(clienteId, limite);
    }
}