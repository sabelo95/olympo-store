package com.compras_service.domain.gateways.Pedido;

import com.compras_service.domain.model.DetallePedido;

public interface DetallePedidoGateway {

    DetallePedido crearDetallePedido(DetallePedido detallePedido);

    DetallePedido actualizarDetallePedido(DetallePedido detallePedido);

    DetallePedido obtenerDetallePedidoPorId(Long id);

    void eliminarDetallePedido(Long id);
}