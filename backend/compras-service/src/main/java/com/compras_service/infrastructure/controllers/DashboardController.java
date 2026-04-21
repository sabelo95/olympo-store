package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.ProductoVenta;
import com.compras_service.domain.usecase.DashboardUseCases.ObtenerDashboardUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ResumenDashboardDTO;
import com.compras_service.infrastructure.adapters.DTOs.VentasPeriodoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
@Tag(name = "Dashboard", description = "API de estadísticas para el panel de administración")
public class DashboardController {

    private final ObtenerDashboardUseCase obtenerDashboardUseCase;

    @GetMapping("/resumen")
    @Operation(summary = "Resumen general", description = "Retorna KPIs principales: total pedidos, ingresos totales, pedidos por estado y total de clientes")
    public ResponseEntity<ResumenDashboardDTO> obtenerResumen() {
        try {
            ResumenDashboardDTO resumen = ResumenDashboardDTO.builder()
                    .totalPedidos(obtenerDashboardUseCase.obtenerTotalPedidos())
                    .totalIngresos(obtenerDashboardUseCase.obtenerTotalIngresos())
                    .pedidosPorEstado(obtenerDashboardUseCase.obtenerPedidosPorEstado())
                    .totalClientes(obtenerDashboardUseCase.obtenerTotalClientes())
                    .build();
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pedidos-recientes")
    @Operation(summary = "Pedidos recientes", description = "Retorna los últimos N pedidos ordenados por fecha descendente")
    public ResponseEntity<List<Pedido>> obtenerPedidosRecientes(
            @Parameter(description = "Cantidad de pedidos a retornar", example = "10")
            @RequestParam(defaultValue = "10") int limite) {
        try {
            return ResponseEntity.ok(obtenerDashboardUseCase.obtenerPedidosRecientes(limite));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top-productos")
    @Operation(summary = "Top productos vendidos", description = "Retorna los N productos más vendidos con su cantidad total vendida")
    public ResponseEntity<List<ProductoVenta>> obtenerTopProductos(
            @Parameter(description = "Cantidad de productos a retornar", example = "5")
            @RequestParam(defaultValue = "5") int limite) {
        try {
            return ResponseEntity.ok(obtenerDashboardUseCase.obtenerTopProductos(limite));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ventas-por-periodo")
    @Operation(
            summary = "Ventas por período",
            description = "Retorna los pedidos completados en un rango de fechas junto con el total de ingresos. Formato de fecha: yyyy-MM-ddTHH:mm:ss (ej: 2024-01-01T00:00:00)"
    )
    public ResponseEntity<VentasPeriodoDTO> obtenerVentasPorPeriodo(
            @Parameter(description = "Fecha de inicio (ISO 8601)", example = "2024-01-01T00:00:00")
            @RequestParam String fechaInicio,
            @Parameter(description = "Fecha de fin (ISO 8601)", example = "2024-12-31T23:59:59")
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);

            List<Pedido> pedidos = obtenerDashboardUseCase.obtenerPedidosPorPeriodo(inicio, fin);
            Double ingresos = obtenerDashboardUseCase.obtenerIngresosEnPeriodo(inicio, fin);

            VentasPeriodoDTO resultado = VentasPeriodoDTO.builder()
                    .totalPedidos(pedidos.size())
                    .totalIngresos(ingresos)
                    .pedidos(pedidos)
                    .build();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
