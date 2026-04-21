package com.compras_service.domain.usecase.PedidoUseCases;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.services.StockService;
import com.compras_service.domain.validators.PedidoValidator;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class EliminarPedidoUseCase {

    private final PedidoGateway pedidoGateway;
    private final PedidoValidator pedidoValidator;
    private final StockService stockService;

    public void eliminarPedido(Long id) {

        Pedido pedido = pedidoGateway.obtenerPedidoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El pedido con id " + id + " no existe"
                ));


        pedidoValidator.validarEliminacion(pedido);


       stockService.reponerStockDelPedido(pedido);

        pedidoGateway.eliminarPedido(id);
    }

}
