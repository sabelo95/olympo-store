package com.compras_service.infrastructure.adapters.mapper.web;

import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.infrastructure.adapters.DTOs.CarritoProductoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CarritoProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class CarritoProductoWebMapper {

    public CarritoProducto toDomain(CarritoProductoRequest request) {
        if (request == null) return null;

        return CarritoProducto.builder()
                .producto_id(request.getProductoId())
                .cantidad(request.getCantidad())
                .build();
    }

    public CarritoProductoResponse toResponse(CarritoProducto domain) {
        if (domain == null) return null;

        CarritoProductoResponse response = new CarritoProductoResponse();
        response.setId(domain.getId());
        response.setProductoId(domain.getProducto_id());
        response.setCantidad(domain.getCantidad());
        return response;
    }
}
