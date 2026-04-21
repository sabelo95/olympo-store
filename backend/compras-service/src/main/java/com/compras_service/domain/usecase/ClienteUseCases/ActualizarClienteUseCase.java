package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class
ActualizarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public Cliente actualizarCliente(Cliente cliente) {

        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }

        if (cliente.getNit() == null || cliente.getNit().isEmpty()) {
            throw new IllegalArgumentException("El NIT del cliente no puede estar vacío.");
        }

        return clienteGateway.actualizarCliente(cliente)
                .orElseThrow(() -> new IllegalArgumentException("No se pudo actualizar el cliente con ID " + cliente.getId()));
    }
}
