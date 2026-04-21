package com.compras_service.domain.gateways.Pedido;

import com.compras_service.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoGateway {

    Pedido crearPedido(Pedido pedido);

    Pedido actualizarPedido(Pedido pedido);

    Optional<Pedido> obtenerPedidoPorId(Long id);

    void eliminarPedido(Long id);

    void actualizarEstadoPedido(Long id, String estado);

    Optional<List<Pedido>> obtenerPedidosCancelados();

    Optional<List<Pedido>> obtenerPedidosCompletados();

    Optional<List<Pedido>> obtenerPedidosEnProceso();

    Optional<List<Pedido>> obtenerPedidosPorClienteId(Long clienteId);

    List<Pedido> obtenerTodos();



}
