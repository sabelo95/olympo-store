package com.compras_service.domain.usecase.ReportesUseCases;

import com.compras_service.config.JwtUtil;
import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.domain.model.Pedido;
import com.compras_service.infrastructure.adapters.External.NotificacionService;
import com.compras_service.infrastructure.adapters.External.ReporteService;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
public class GenerarReportesUseCase {

    private final PedidoGateway pedidoGateway;
    private final ReporteService pdfReportService;
    private final NotificacionService notificacionService;
    private final JwtUtil jwtUtil;

    public byte[] ejecutar(String fechaInicio, String fechaFin) {

        List<Pedido> pedidos = pedidoGateway.obtenerPedidosCompletados()
                .orElseThrow(() -> new RuntimeException("No se encontraron pedidos"));


        LocalDateTime inicio = LocalDate.parse(fechaInicio).atStartOfDay();
        LocalDateTime fin = LocalDate.parse(fechaFin).atTime(LocalTime.MAX);


        List<Pedido> pedidosFiltrados = pedidos.stream()
                .filter(p -> p.getFechaPedido() != null)
                .filter(p -> p.getEstado() == EstadoPedido.COMPLETADO)
                .filter(p -> !p.getFechaPedido().isBefore(inicio) && !p.getFechaPedido().isAfter(fin))
                .toList();


        byte[] pdf = pdfReportService.generarReporteSemanal(pedidosFiltrados, fechaInicio, fechaFin);


        try {
            String correoUsuario = jwtUtil.obtenerCorreoActual();
            notificacionService.enviarNotificacionConAdjunto(
                    "Reporte de Ventas Semanal",
                    correoUsuario,
                    "Adjunto encontrarás el reporte de ventas del período " + fechaInicio + " al " + fechaFin + ".",
                    pdf,
                    "reporte_ventas_semanal.pdf"
            );
        } catch (Exception e) {
            System.out.println("⚠️ Error enviando notificación: " + e.getMessage());
        }


        return pdf;
    }



}
