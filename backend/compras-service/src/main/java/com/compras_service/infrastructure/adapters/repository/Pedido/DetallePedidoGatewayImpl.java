package com.compras_service.infrastructure.adapters.repository.Pedido;

import com.compras_service.domain.model.DetallePedido;
import com.compras_service.domain.gateways.Pedido.DetallePedidoGateway;
import com.compras_service.infrastructure.adapters.mapper.DetallePedidoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@AllArgsConstructor
public class DetallePedidoGatewayImpl implements DetallePedidoGateway {

    private final DetallePedidoRepository detallePedidoRepository;
    private final DetallePedidoMapper detallePedidoMapper;

    @Override
    public DetallePedido crearDetallePedido(DetallePedido detallePedido) {
        var detallePedidoEntity = detallePedidoMapper.toEntity(detallePedido);
        var detallePedidoCreado = detallePedidoRepository.save(detallePedidoEntity);
        return detallePedidoMapper.toDomain(detallePedidoCreado);
    }

    @Override
    public DetallePedido actualizarDetallePedido(DetallePedido detallePedido) {
        return null;
    }

    @Override
    public DetallePedido obtenerDetallePedidoPorId(Long id) {
        return null;
    }

    @Override
    public void eliminarDetallePedido(Long id) {

    }
}
