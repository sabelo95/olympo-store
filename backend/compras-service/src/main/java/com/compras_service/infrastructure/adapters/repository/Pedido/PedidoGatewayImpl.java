package com.compras_service.infrastructure.adapters.repository.Pedido;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.infrastructure.adapters.entity.PedidoEntity;
import com.compras_service.infrastructure.adapters.mapper.PedidoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PedidoGatewayImpl implements PedidoGateway {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public Pedido crearPedido(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoMapper.toEntity(pedido);
        Pedido pedidoSaved = pedidoMapper.toDomain(pedidoRepository.save(pedidoEntity));
        return pedidoSaved;

    }

    @Override
    public Pedido actualizarPedido(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoMapper.toEntity(pedido);
        Pedido pedidoUpdated = pedidoMapper.toDomain(pedidoRepository.save(pedidoEntity));
        return pedidoUpdated;
    }

    @Override
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toDomain);
    }


    @Override
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);

    }


    @Override
    public void actualizarEstadoPedido(Long id, String estado) {
        Optional<PedidoEntity> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            PedidoEntity pedido = pedidoOpt.get();
            pedido.setEstado(EstadoPedido.valueOf(estado));
            pedidoRepository.save(pedido);
        }

    }

    @Override
    public Optional<List<Pedido>> obtenerPedidosCancelados() {
        Optional<List<PedidoEntity>> pedidosEncontrados= pedidoRepository.findByEstado(EstadoPedido.CANCELADO);
        return  pedidosEncontrados.map(lista -> lista.stream().map(pedidoMapper::toDomain).toList());
    }

    @Override
    public Optional<List<Pedido>> obtenerPedidosCompletados() {
        Optional<List<PedidoEntity>> pedidosEncontrados= pedidoRepository.findByEstado(EstadoPedido.COMPLETADO);
        return  pedidosEncontrados.map(lista -> lista.stream().map(pedidoMapper::toDomain).toList());
    }

    @Override
    public Optional<List<Pedido>> obtenerPedidosEnProceso() {
        Optional<List<PedidoEntity>> pedidosEncontrados= pedidoRepository.findByEstado(EstadoPedido.EN_PROCESO);
        return  pedidosEncontrados.map(lista -> lista.stream().map(pedidoMapper::toDomain).toList());
    }

    @Override
    public Optional<List<Pedido>> obtenerPedidosPorClienteId(Long clienteId) {
        List<PedidoEntity> pedidosEncontrados = pedidoRepository.findByClienteId(clienteId);
        List<Pedido> pedidos = pedidosEncontrados.stream()
                .map(pedidoMapper::toDomain)
                .toList();

        return pedidos.isEmpty() ? Optional.empty() : Optional.of(pedidos);
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDomain)
                .toList();
    }
}
