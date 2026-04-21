package com.compras_service.infrastructure.adapters.repository.Dashboard;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.DashboardGateway;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.ProductoVenta;
import com.compras_service.infrastructure.adapters.mapper.PedidoMapper;
import com.compras_service.infrastructure.adapters.repository.Cliente.ClienteRepository;
import com.compras_service.infrastructure.adapters.repository.Pedido.DetallePedidoRepository;
import com.compras_service.infrastructure.adapters.repository.Pedido.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class DashboardGatewayImpl implements DashboardGateway {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public long contarTotalPedidos() {
        return pedidoRepository.count();
    }

    @Override
    public long contarPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }

    @Override
    public Double sumaTotalIngresos() {
        Double suma = pedidoRepository.sumTotalCompletados();
        return suma != null ? suma : 0.0;
    }

    @Override
    public List<Pedido> obtenerPedidosRecientes(int limite) {
        return pedidoRepository.findTopByOrderByFechaPedidoDesc(PageRequest.of(0, limite))
                .stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> obtenerPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaPedidoBetween(inicio, fin)
                .stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }

    @Override
    public Double sumaTotalIngresosEnPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Double suma = pedidoRepository.sumTotalCompletadosEnPeriodo(inicio, fin);
        return suma != null ? suma : 0.0;
    }

    @Override
    public List<ProductoVenta> obtenerTopProductos(int limite) {
        return detallePedidoRepository.findTopProductos(PageRequest.of(0, limite))
                .stream()
                .map(row -> new ProductoVenta((Long) row[0], ((Number) row[1]).longValue()))
                .toList();
    }

    @Override
    public long contarClientes() {
        return clienteRepository.count();
    }
}
