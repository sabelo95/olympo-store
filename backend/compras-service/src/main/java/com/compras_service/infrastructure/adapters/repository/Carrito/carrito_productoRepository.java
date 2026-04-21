package com.compras_service.infrastructure.adapters.repository.Carrito;

import com.compras_service.infrastructure.adapters.entity.CarritoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface carrito_productoRepository extends JpaRepository<CarritoProductoEntity, Long> {
}
