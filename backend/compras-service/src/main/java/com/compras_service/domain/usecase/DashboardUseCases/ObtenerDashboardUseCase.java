package com.compras_service.domain.usecase.DashboardUseCases;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.DashboardGateway;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.ProductoVenta;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ObtenerDashboardUseCase {

    private final DashboardGateway dashboardGateway;

    public long obtenerTotalPedidos() {
        return dashboardGateway.contarTotalPedidos();
    }

    public Double obtenerTotalIngresos() {
        return dashboardGateway.sumaTotalIngresos();
    }

    public long obtenerTotalClientes() {
        return dashboardGateway.contarClientes();
    }

    public Map<String, Long> obtenerPedidosPorEstado() {
        Map<String, Long> resultado = new LinkedHashMap<>();
        for (EstadoPedido estado : EstadoPedido.values()) {
            resultado.put(estado.name(), dashboardGateway.contarPedidosPorEstado(estado));
        }
        return resultado;
    }

    public List<Pedido> obtenerPedidosRecientes(int limite) {
        return dashboardGateway.obtenerPedidosRecientes(limite);
    }

    public List<ProductoVenta> obtenerTopProductos(int limite) {
        return dashboardGateway.obtenerTopProductos(limite);
    }

    public List<Pedido> obtenerPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return dashboardGateway.obtenerPedidosPorPeriodo(inicio, fin);
    }

    public Double obtenerIngresosEnPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return dashboardGateway.sumaTotalIngresosEnPeriodo(inicio, fin);
    }
}
