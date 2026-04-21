package com.compras_service.infrastructure.adapters.mapper.web;

import com.compras_service.domain.model.Carrito;
import com.compras_service.infrastructure.adapters.DTOs.CarritoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CarritoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarritoWebMapper {

    private final CarritoProductoWebMapper carritoProductoWebMapper;

    public Carrito toDomain(CarritoRequest request) {
        if (request == null) return null;

        return Carrito.builder()
                .cliente_id(request.getClienteId())
                .abandonado(request.getAbandonado())
                .productos(request.getProductos() != null
                        ? request.getProductos().stream()
                        .map(carritoProductoWebMapper::toDomain)
                        .collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .build();
    }

    public CarritoResponse toResponse(Carrito domain) {
        if (domain == null) return null;

        CarritoResponse response = new CarritoResponse();
        response.setId(domain.getId());
        response.setClienteId(domain.getCliente_id());
        response.setFechaActualizacion(domain.getFecha_actualizacion());
        response.setAbandonado(domain.getAbandonado());
        response.setProductos(domain.getProductos() != null
                ? domain.getProductos().stream()
                .map(carritoProductoWebMapper::toResponse)
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>());

        return response;
    }
}
