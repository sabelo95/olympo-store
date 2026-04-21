package com.compras_service.infrastructure.adapters.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "carrito_producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoProductoEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id") // columna FK en la tabla carrito_producto
    private CarritoEntity carrito;
    private Long producto_id;
    private String marca;
    private Integer cantidad;
}
