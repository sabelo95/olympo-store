package com.compras_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoProducto {

    private Long id;
    private Carrito carrito;
    private Long producto_id;
    private Integer cantidad;

}
