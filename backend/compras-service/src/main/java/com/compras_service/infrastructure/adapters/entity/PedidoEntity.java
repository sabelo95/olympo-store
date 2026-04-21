package com.compras_service.infrastructure.adapters.entity;

import com.compras_service.domain.Enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "pedido") // asegura el nombre exacto de la tabla
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    private Double total;

    @Column(name = "fecha_entrega_estimado")
    private LocalDateTime fechaEntregaEstimado;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "direccion_envio" )
    private String direccionEnvio;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoEntity> detallePedido;

    @Column(name = "Metodo_pago")
    private String metodoPago;
}
