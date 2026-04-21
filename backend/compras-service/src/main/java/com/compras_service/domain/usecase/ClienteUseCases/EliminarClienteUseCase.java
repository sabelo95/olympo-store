package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EliminarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public void eliminarCliente(String nit) {
        if (nit == null) {
            throw new IllegalArgumentException("El NIT no puede ser nulo.");
        }

        if (clienteGateway.obtenerClientePorNit(nit).isEmpty()) {
            throw new IllegalArgumentException("No existe un cliente con NIT " + nit);
        }

        clienteGateway.eliminarCliente(nit);
    }
}

