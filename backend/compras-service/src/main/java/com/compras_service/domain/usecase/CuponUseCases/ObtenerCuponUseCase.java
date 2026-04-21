package com.compras_service.domain.usecase.CuponUseCases;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import com.compras_service.domain.model.Cupon;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ObtenerCuponUseCase {

    private final CuponGateway cuponGateway;

    public List<Cupon> obtenerTodos() {
        return cuponGateway.obtenerTodos();
    }

    public Cupon obtenerPorCodigo(String codigo) {
        return cuponGateway.obtenerPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado con código: " + codigo));
    }
}
