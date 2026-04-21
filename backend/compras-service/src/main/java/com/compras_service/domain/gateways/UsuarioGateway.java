package com.compras_service.domain.gateways;

import com.compras_service.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioGateway {
    Optional<Usuario> obtenerUsuarioPorId(Long id);
}

