package com.compras_service.domain.usecase.CuponUseCases;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import com.compras_service.domain.model.Cupon;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActualizarCuponUseCase {

    private final CuponGateway cuponGateway;

    public Cupon actualizar(String codigo, Cupon datos) {
        Cupon existente = cuponGateway.obtenerPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado con código: " + codigo));

        if (datos.getDescuentoPorcentaje() == null && datos.getDescuentoFijo() == null) {
            throw new IllegalArgumentException("Debe especificar descuentoPorcentaje o descuentoFijo.");
        }

        if (datos.getDescuentoPorcentaje() != null && datos.getDescuentoFijo() != null) {
            throw new IllegalArgumentException("No puede tener descuentoPorcentaje y descuentoFijo al mismo tiempo.");
        }

        existente.setDescuentoPorcentaje(datos.getDescuentoPorcentaje());
        existente.setDescuentoFijo(datos.getDescuentoFijo());
        existente.setActivo(datos.isActivo());
        existente.setFechaExpiracion(datos.getFechaExpiracion());
        existente.setUsoMaximo(datos.getUsoMaximo());
        existente.setMontoMinimo(datos.getMontoMinimo());

        return cuponGateway.guardar(existente);
    }
}
