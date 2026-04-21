package com.compras_service.infrastructure.adapters.mapper.web;

import com.compras_service.domain.model.Cliente;
import com.compras_service.infrastructure.adapters.DTOs.ClienteRequest;
import com.compras_service.infrastructure.adapters.DTOs.ClienteResponse;
import org.springframework.stereotype.Component;

@Component
public class ClienteWebMapper {

    public Cliente toDomain(ClienteRequest request) {
        if (request == null) return null;

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setNit(request.getNit());
        cliente.setCiudad(request.getCiudad());
        cliente.setPais(request.getPais());
        cliente.setUsuario_id(request.getUsuarioId());
        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) return null;

        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setNit(cliente.getNit());
        response.setCiudad(cliente.getCiudad());
        response.setPais(cliente.getPais());
        response.setUsuarioId(cliente.getUsuario_id());
        return response;
    }
}
