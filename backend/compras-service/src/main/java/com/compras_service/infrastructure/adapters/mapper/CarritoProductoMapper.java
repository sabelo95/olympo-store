package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.infrastructure.adapters.entity.CarritoProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class CarritoProductoMapper {

    public CarritoProducto toDomain(CarritoProductoEntity entity) {
        if (entity == null) {
            return null;
        }


        return CarritoProducto.builder()
                .id(entity.getId())
                .producto_id(entity.getProducto_id())
                .cantidad(entity.getCantidad())
                .build();
    }

    public CarritoProductoEntity toEntity(CarritoProducto domain) {
        if (domain == null) {
            return null;
        }


        return CarritoProductoEntity.builder()
                .id(domain.getId())
                .producto_id(domain.getProducto_id())
                .cantidad(domain.getCantidad())
                .build();
    }
}
