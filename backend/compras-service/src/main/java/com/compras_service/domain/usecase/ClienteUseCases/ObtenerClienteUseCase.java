package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerClienteUseCase {

    private final ClienteGateway clienteGateway;

    public Cliente obtenerPorNit(String nit) {
        return clienteGateway.obtenerClientePorNit(nit)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un cliente con Nit " + nit));
    }

    public Cliente obtenerPorUsuarioId(Long usuarioId) {
        return clienteGateway.obtenerClientePorUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un cliente con usuarioId " + usuarioId));
    }
}
