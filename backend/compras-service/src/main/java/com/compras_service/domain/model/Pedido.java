package com.compras_service.domain.model;

import com.compras_service.domain.Enums.EstadoPedido;
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
public class Pedido {

   private Long id;
   private Long clienteId;
   private LocalDateTime fechaPedido;
   private EstadoPedido estado;
   private Double total;
   private LocalDateTime fechaEntregaEstimada;
   private LocalDateTime fechaEntregaReal;
   private String direccionEnvio;
   private List<DetallePedido> detallePedido;
   private String metodoPago;


}
