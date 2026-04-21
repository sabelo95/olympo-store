package com.compras_service.infrastructure.adapters.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarritoProductoResponse {

    private Long id;
    private Long productoId;
    private Integer cantidad;
}
