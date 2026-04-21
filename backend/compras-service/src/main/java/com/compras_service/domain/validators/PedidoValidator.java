package com.compras_service.domain.validators;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;

public class PedidoValidator {

    public void validarEstadoEditable(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.CREADO) {
            throw new IllegalArgumentException("Solo se pueden actualizar pedidos en estado CREADO");
        }
    }

    public void validarEliminacion(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.CREADO) {
            throw new IllegalArgumentException(
                    "Solo se pueden eliminar pedidos en estado CREADO"
            );
        }
    }
}

