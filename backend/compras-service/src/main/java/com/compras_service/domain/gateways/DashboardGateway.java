package com.compras_service.domain.gateways;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.ProductoVenta;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardGateway {

    long contarTotalPedidos();

    long contarPedidosPorEstado(EstadoPedido estado);

    Double sumaTotalIngresos();

    List<Pedido> obtenerPedidosRecientes(int limite);

    List<Pedido> obtenerPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fin);

    Double sumaTotalIngresosEnPeriodo(LocalDateTime inicio, LocalDateTime fin);

    List<ProductoVenta> obtenerTopProductos(int limite);

    long contarClientes();
}
