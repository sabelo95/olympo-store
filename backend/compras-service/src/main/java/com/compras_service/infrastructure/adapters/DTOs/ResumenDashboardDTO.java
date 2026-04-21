package com.compras_service.infrastructure.adapters.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumenDashboardDTO {
    private long totalPedidos;
    private Double totalIngresos;
    private Map<String, Long> pedidosPorEstado;
    private long totalClientes;
}
