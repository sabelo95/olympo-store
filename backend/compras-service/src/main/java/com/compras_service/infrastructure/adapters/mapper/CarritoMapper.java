package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.Carrito;
import com.compras_service.infrastructure.adapters.entity.CarritoEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    private final CarritoProductoMapper carritoProductoMapper;

    public CarritoMapper(CarritoProductoMapper carritoProductoMapper) {
        this.carritoProductoMapper = carritoProductoMapper;
    }

    public Carrito toDomain(CarritoEntity entity) {
        if (entity == null) {
            return null;
        }
        return Carrito.builder()
                .id(entity.getId())
                .cliente_id(entity.getClienteId())
                .fecha_actualizacion(entity.getFechaActualizacion())
                .abandonado(entity.getAbandonado())
                .productos(entity.getProductos() != null
                        ? entity.getProductos().stream()
                        .map(carritoProductoMapper::toDomain)
                        .collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .build();
    }

    public CarritoEntity toEntity(Carrito domain) {
        if (domain == null) {
            return null;
        }

        CarritoEntity carritoEntity = CarritoEntity.builder()
                .id(domain.getId())
                .clienteId(domain.getCliente_id())
                .fechaActualizacion(domain.getFecha_actualizacion())
                .abandonado(domain.getAbandonado())
                .productos(new ArrayList<>())
                .build();

        if (domain.getProductos() != null) {
            carritoEntity.getProductos().addAll(
                    domain.getProductos().stream()
                            .map(producto -> {
                                var entity = carritoProductoMapper.toEntity(producto);
                                entity.setCarrito(carritoEntity);
                                return entity;
                            })
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        return carritoEntity;
    }
}


