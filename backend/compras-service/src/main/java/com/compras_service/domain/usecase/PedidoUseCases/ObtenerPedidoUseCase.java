package com.compras_service.domain.usecase.PedidoUseCases;

import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.domain.model.Pedido;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ObtenerPedidoUseCase {

    private final PedidoGateway pedidoGateway;

    public List<Pedido> obtenerTodos() {
        return pedidoGateway.obtenerTodos();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoGateway.obtenerPedidoPorId(id);
    }

    public Optional<List<Pedido>> obtenerPorClienteId(Long clienteId) {
        return pedidoGateway.obtenerPedidosPorClienteId(clienteId);
    }

    public Optional<List<Pedido>> obtenerCancelados() {
        return pedidoGateway.obtenerPedidosCancelados();
    }

    public Optional<List<Pedido>> obtenerCompletados() {
        return pedidoGateway.obtenerPedidosCompletados();
    }

    public Optional<List<Pedido>> obtenerEnProceso() {
        return pedidoGateway.obtenerPedidosEnProceso();
    }
}
