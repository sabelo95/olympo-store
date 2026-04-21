package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.Cliente;
import com.compras_service.infrastructure.adapters.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getNit(),
            cliente.getCiudad(),
            cliente.getPais(),
            cliente.getUsuario_id()
        );
    }

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
            entity.getId(),
            entity.getNombre(),
            entity.getNit(),
            entity.getCiudad(),
            entity.getPais(),
            entity.getUsuario_id()
        );
    }
}
