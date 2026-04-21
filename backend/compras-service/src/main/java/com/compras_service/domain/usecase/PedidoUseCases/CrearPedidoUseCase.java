package com.compras_service.domain.usecase.PedidoUseCases;

import com.compras_service.domain.model.Pedido;
import com.compras_service.service.NotificacionPedidoService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CrearPedidoUseCase {

    private final PedidoServiceUseCase pedidoService;
    private final NotificacionPedidoService notificacionPedidoService;

    public Pedido ejecutar(Long clienteId, Long carritoId, String direccionEnvio, String metodoPago) {
        Pedido pedido = pedidoService.crearPedidoDesdeCarrito(clienteId, carritoId, direccionEnvio, metodoPago);
        notificacionPedidoService.enviarNotificacionPedidoCreado(pedido);
        return pedido;
    }
}

