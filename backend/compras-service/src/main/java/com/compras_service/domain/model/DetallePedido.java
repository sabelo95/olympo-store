package com.compras_service.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedido {

    private Long id;
    private Long pedidoId;
    private Long producto_id;
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal;
}
