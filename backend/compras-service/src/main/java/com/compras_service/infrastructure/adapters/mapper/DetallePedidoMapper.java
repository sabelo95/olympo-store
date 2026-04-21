package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.DetallePedido;
import com.compras_service.infrastructure.adapters.entity.DetallePedidoEntity;
import com.compras_service.infrastructure.adapters.entity.PedidoEntity;
import org.springframework.stereotype.Component;

@Component
public class DetallePedidoMapper {

    public DetallePedido toDomain(DetallePedidoEntity detallePedidoEntity) {
        return DetallePedido.builder()
                .id(detallePedidoEntity.getId())
                .pedidoId(detallePedidoEntity.getPedido().getId())
                .producto_id(detallePedidoEntity.getProducto_id())
                .cantidad(detallePedidoEntity.getCantidad())
                .precio_unitario(detallePedidoEntity.getPrecio_unitario())
                .subtotal(detallePedidoEntity.getSubtotal())
                .build();
    }

    public DetallePedidoEntity toEntity(DetallePedido detallePedido) {
        DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
        detallePedidoEntity.setId(detallePedido.getId());


        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(detallePedido.getPedidoId());
        detallePedidoEntity.setPedido(pedidoEntity);

        detallePedidoEntity.setProducto_id(detallePedido.getProducto_id());
        detallePedidoEntity.setCantidad(detallePedido.getCantidad());
        detallePedidoEntity.setPrecio_unitario(detallePedido.getPrecio_unitario());
        detallePedidoEntity.setSubtotal(detallePedido.getSubtotal());
        return detallePedidoEntity;
    }
}
