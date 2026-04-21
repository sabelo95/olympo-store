package com.compras_service.infrastructure.adapters.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CarritoResponse {

    private Long id;
    private Long clienteId;
    private LocalDateTime fechaActualizacion;
    private Boolean abandonado;
    private List<CarritoProductoResponse> productos;
}
