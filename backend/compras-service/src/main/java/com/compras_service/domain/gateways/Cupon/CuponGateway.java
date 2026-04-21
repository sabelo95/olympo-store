package com.compras_service.domain.gateways.Cupon;

import com.compras_service.domain.model.Cupon;

import java.util.List;
import java.util.Optional;

public interface CuponGateway {
    Optional<Cupon> obtenerPorCodigo(String codigo);
    Cupon guardar(Cupon cupon);
    List<Cupon> obtenerTodos();
    void eliminar(String codigo);
}
