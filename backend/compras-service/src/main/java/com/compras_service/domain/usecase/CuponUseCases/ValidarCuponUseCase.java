package com.compras_service.domain.usecase.CuponUseCases;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import com.compras_service.domain.model.Cupon;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ValidarCuponUseCase {

    private final CuponGateway cuponGateway;

    public Cupon validar(String codigo, Long total) {
        Cupon cupon = cuponGateway.obtenerPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Cupón inválido o expirado"));

        if (!cupon.isActivo()) {
            throw new IllegalArgumentException("Cupón inválido o expirado");
        }

        if (cupon.getFechaExpiracion() != null && cupon.getFechaExpiracion().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cupón inválido o expirado");
        }

        if (cupon.getUsoMaximo() != null && cupon.getUsoActual() >= cupon.getUsoMaximo()) {
            throw new IllegalArgumentException("Cupón ya no tiene usos disponibles");
        }

        if (cupon.getMontoMinimo() != null && total < cupon.getMontoMinimo()) {
            throw new IllegalArgumentException(
                    "El monto mínimo para este cupón es $" + String.format("%,.0f", cupon.getMontoMinimo().doubleValue())
            );
        }

        cupon.setUsoActual(cupon.getUsoActual() + 1);
        cuponGateway.guardar(cupon);

        return cupon;
    }
}
