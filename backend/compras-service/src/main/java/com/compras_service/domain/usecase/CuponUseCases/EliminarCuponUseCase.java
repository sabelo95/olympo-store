package com.compras_service.domain.usecase.CuponUseCases;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EliminarCuponUseCase {

    private final CuponGateway cuponGateway;

    public void eliminar(String codigo) {
        cuponGateway.obtenerPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado con código: " + codigo));

        cuponGateway.eliminar(codigo);
    }
}
