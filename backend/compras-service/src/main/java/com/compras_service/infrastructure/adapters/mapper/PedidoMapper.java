package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.Pedido;
import com.compras_service.infrastructure.adapters.entity.PedidoEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class PedidoMapper {

    private final DetallePedidoMapper detallePedidoMapper;

    public  Pedido toDomain(PedidoEntity pedidoEntity) {

        return Pedido.builder()
                .id(pedidoEntity.getId())
                .clienteId(pedidoEntity.getClienteId())
                .fechaPedido(pedidoEntity.getFechaPedido())
                .estado(pedidoEntity.getEstado())
                .total(pedidoEntity.getTotal())
                .fechaEntregaEstimada(pedidoEntity.getFechaEntregaEstimado())
                .fechaEntregaReal(pedidoEntity.getFechaEntregaReal())
                .direccionEnvio(pedidoEntity.getDireccionEnvio())
                .detallePedido(pedidoEntity.getDetallePedido().stream().map(detallePedidoMapper::toDomain).collect(Collectors.toList()))
                .metodoPago(pedidoEntity.getMetodoPago())
                .build();
    }

    public  PedidoEntity toEntity(Pedido pedido) {

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(pedido.getId());
        pedidoEntity.setClienteId(pedido.getClienteId());
        pedidoEntity.setFechaPedido(pedido.getFechaPedido());
        pedidoEntity.setEstado(pedido.getEstado());
        pedidoEntity.setTotal(pedido.getTotal());
        pedidoEntity.setFechaEntregaEstimado(pedido.getFechaEntregaEstimada());
        pedidoEntity.setFechaEntregaReal(pedido.getFechaEntregaReal());
        pedidoEntity.setDireccionEnvio(pedido.getDireccionEnvio());
        pedidoEntity.setDetallePedido(pedido.getDetallePedido().stream().map(detallePedidoMapper::toEntity).collect(Collectors.toList()));
        pedidoEntity.setMetodoPago(pedido.getMetodoPago());
        return pedidoEntity;
    }

}
