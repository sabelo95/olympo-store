package com.compras_service.infrastructure.adapters.repository.Cupon;

import com.compras_service.infrastructure.adapters.entity.CuponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<CuponEntity, Long> {
    Optional<CuponEntity> findByCodigo(String codigo);
}
