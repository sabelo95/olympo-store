package com.compras_service.domain.services;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;

import java.time.LocalDateTime;

public class EstadoPedidoService {

    public void cambiarEstado(Pedido pedido, EstadoPedido nuevoEstado) {

        EstadoPedido actual = pedido.getEstado();

        switch (actual) {

            case CREADO:
                if (nuevoEstado != EstadoPedido.EN_PROCESO &&
                        nuevoEstado != EstadoPedido.CANCELADO) {
                    throw new IllegalArgumentException("Transición inválida desde CREADO.");
                }
                break;

            case EN_PROCESO:
                if (nuevoEstado != EstadoPedido.COMPLETADO &&
                        nuevoEstado != EstadoPedido.CANCELADO) {
                    throw new IllegalArgumentException("Transición inválida desde EN_PROCESO.");
                }
                break;

            case COMPLETADO:
                throw new IllegalArgumentException("El pedido ya está COMPLETADO y no puede cambiar.");

            case CANCELADO:
                throw new IllegalArgumentException("El pedido ya está CANCELADO y no puede cambiar.");
        }

        if (nuevoEstado == EstadoPedido.COMPLETADO) {
            pedido.setFechaEntregaReal(LocalDateTime.now());
        }

        pedido.setEstado(nuevoEstado);
    }
}

