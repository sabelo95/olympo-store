package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.model.Usuario;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearClienteUseCase {

    private final ClienteGateway clienteGateway;
    private final UsuarioGateway usuarioGateway;

    public Cliente crearCliente(Cliente cliente) {

        Usuario usuario = usuarioGateway.obtenerUsuarioPorId(cliente.getUsuario_id())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        if (!usuario.getRol().equals("CLIENTE")) {
            throw new IllegalArgumentException("El usuario no tiene rol CLIENTE.");
        }

       if (clienteGateway.obtenerClientePorNit(cliente.getNit()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con el NIT " + cliente.getNit());
        }



        if(cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }

        if (cliente.getNit() == null) {
            throw new IllegalArgumentException("El NIT del cliente no puede estar vacío.");
        }

        if (cliente.getCiudad() == null || cliente.getCiudad().isEmpty()) {
            throw new IllegalArgumentException("La ciudad del cliente no puede estar vacía.");
        }

        if (cliente.getPais() == null || cliente.getPais().isEmpty()) {
            throw new IllegalArgumentException("El país del cliente no puede estar vacío.");
        }

        return clienteGateway.crearCliente(cliente);
    }
}
