package com.compras_service.domain.gateways.Cliente;

import com.compras_service.domain.model.Cliente;

import java.util.Optional;

public interface ClienteGateway {

    Cliente crearCliente(Cliente cliente);
    Optional<Cliente> obtenerClientePorId(Long id);
    Optional<Cliente> actualizarCliente(Cliente cliente);
    Optional<Cliente> obtenerClientePorNit(String nit);
    Optional<String> obtenerEmailPorId(double id);
    Optional<Cliente> obtenerClientePorUsuarioId(Long usuarioId);
    void eliminarCliente(String nit);


}
