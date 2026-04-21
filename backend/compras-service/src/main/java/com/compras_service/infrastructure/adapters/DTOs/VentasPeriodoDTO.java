package com.compras_service.infrastructure.adapters.DTOs;

import com.compras_service.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentasPeriodoDTO {
    private long totalPedidos;
    private Double totalIngresos;
    private List<Pedido> pedidos;
}
