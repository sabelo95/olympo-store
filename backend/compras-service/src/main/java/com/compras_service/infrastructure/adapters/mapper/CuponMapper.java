package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.Cupon;
import com.compras_service.infrastructure.adapters.entity.CuponEntity;
import org.springframework.stereotype.Component;

@Component
public class CuponMapper {

    public CuponEntity toEntity(Cupon cupon) {
        return new CuponEntity(
                cupon.getId(),
                cupon.getCodigo(),
                cupon.getDescuentoPorcentaje(),
                cupon.getDescuentoFijo(),
                cupon.isActivo(),
                cupon.getFechaExpiracion(),
                cupon.getUsoMaximo(),
                cupon.getUsoActual(),
                cupon.getMontoMinimo()
        );
    }

    public Cupon toDomain(CuponEntity entity) {
        return new Cupon(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescuentoPorcentaje(),
                entity.getDescuentoFijo(),
                entity.isActivo(),
                entity.getFechaExpiracion(),
                entity.getUsoMaximo(),
                entity.getUsoActual(),
                entity.getMontoMinimo()
        );
    }
}
