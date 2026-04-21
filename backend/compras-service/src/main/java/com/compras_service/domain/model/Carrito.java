package com.compras_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carrito {

    private Long id;
    private Long cliente_id;
    private LocalDateTime fecha_actualizacion;
    private Boolean abandonado = false;
    private List<CarritoProducto> productos;

}
