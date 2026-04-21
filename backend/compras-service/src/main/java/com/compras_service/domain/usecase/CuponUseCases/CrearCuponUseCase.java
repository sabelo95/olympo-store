package com.compras_service.domain.usecase.CuponUseCases;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import com.compras_service.domain.model.Cupon;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearCuponUseCase {

    private final CuponGateway cuponGateway;

    public Cupon crear(Cupon cupon) {
        if (cuponGateway.obtenerPorCodigo(cupon.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cupón con el código: " + cupon.getCodigo());
        }

        if (cupon.getDescuentoPorcentaje() == null && cupon.getDescuentoFijo() == null) {
            throw new IllegalArgumentException("Debe especificar descuentoPorcentaje o descuentoFijo.");
        }

        if (cupon.getDescuentoPorcentaje() != null && cupon.getDescuentoFijo() != null) {
            throw new IllegalArgumentException("No puede tener descuentoPorcentaje y descuentoFijo al mismo tiempo.");
        }

        if (cupon.getDescuentoPorcentaje() != null && (cupon.getDescuentoPorcentaje() <= 0 || cupon.getDescuentoPorcentaje() > 100)) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 1 y 100.");
        }

        if (cupon.getDescuentoFijo() != null && cupon.getDescuentoFijo() <= 0) {
            throw new IllegalArgumentException("El descuento fijo debe ser mayor a 0.");
        }

        cupon.setUsoActual(0);
        return cuponGateway.guardar(cupon);
    }
}
