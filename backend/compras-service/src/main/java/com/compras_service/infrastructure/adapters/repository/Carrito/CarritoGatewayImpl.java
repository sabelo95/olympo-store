package com.compras_service.infrastructure.adapters.repository.Carrito;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.infrastructure.adapters.entity.CarritoEntity;
import com.compras_service.infrastructure.adapters.mapper.CarritoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarritoGatewayImpl implements CarritoGateway {
    private final CarritoRepository carritoRepository;
    private final CarritoMapper carritoMapper;


    @Override
    public Carrito crearCarrito(Carrito carrito) {
        CarritoEntity carritoEntity = carritoMapper.toEntity(carrito);
        CarritoEntity savedEntity = carritoRepository.save(carritoEntity);
        return carritoMapper.toDomain(savedEntity);
    }

    @Override
    public Carrito actualizarCarrito(Carrito carrito) {
        return carritoMapper.toDomain(carritoRepository.save(carritoMapper.toEntity(carrito)));
    }

    @Override
    public Optional<Carrito> obtenerCarritoPorId(Long id) {
        return carritoRepository.findById(id)
                .map(carritoMapper::toDomain);
    }

    @Override
    public List<Carrito> obtenerCarritosPorClienteId(Long ClienteId) {
        List<CarritoEntity> carritoEntities = carritoRepository.findByClienteId(ClienteId);
        return carritoEntities.stream()
                .map(carritoMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminarCarrito(Long id) {
        if (carritoRepository.findById(id).isPresent()) {
            carritoRepository.deleteById(id);
            }

    }

    @Override
    public List<Carrito> obtenerCarritosAbandonadosYFechaActualizacionBetween(LocalDateTime desde, LocalDateTime hasta) {
        return carritoRepository.findCarritosInactivosEntreHoras(desde, hasta).stream()
                .map(carritoMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Carrito> obtenerCarritoActivoPorCliente(Long clienteId, LocalDateTime limite) {
        return carritoRepository.findCarritoActivoPorCliente(clienteId, limite)
                .map(carritoMapper::toDomain);
    }
}
