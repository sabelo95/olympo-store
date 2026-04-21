package com.compras_service.infrastructure.adapters.repository.Cupon;

import com.compras_service.domain.gateways.Cupon.CuponGateway;
import com.compras_service.domain.model.Cupon;
import com.compras_service.infrastructure.adapters.mapper.CuponMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Transactional
public class CuponGatewayImpl implements CuponGateway {

    private final CuponRepository cuponRepository;
    private final CuponMapper cuponMapper;

    @Override
    public Optional<Cupon> obtenerPorCodigo(String codigo) {
        return cuponRepository.findByCodigo(codigo)
                .map(cuponMapper::toDomain);
    }

    @Override
    public Cupon guardar(Cupon cupon) {
        return cuponMapper.toDomain(
                cuponRepository.save(cuponMapper.toEntity(cupon))
        );
    }

    @Override
    public List<Cupon> obtenerTodos() {
        return cuponRepository.findAll().stream()
                .map(cuponMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(String codigo) {
        cuponRepository.findByCodigo(codigo)
                .ifPresent(entity -> cuponRepository.deleteById(entity.getId()));
    }
}
