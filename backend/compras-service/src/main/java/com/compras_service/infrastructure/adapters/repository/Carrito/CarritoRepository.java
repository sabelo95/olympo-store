package com.compras_service.infrastructure.adapters.repository.Carrito;

import com.compras_service.infrastructure.adapters.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoEntity, Long> {

    public List<CarritoEntity> findByClienteId(Long clienteId);

    Optional<Boolean> findPedidoById(Long id);

    @Query("SELECT c FROM CarritoEntity c WHERE c.abandonado = true AND c.fechaActualizacion BETWEEN :desde AND :hasta")
    List<CarritoEntity> findCarritosInactivosEntreHoras(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT c FROM CarritoEntity c WHERE c.clienteId = :clienteId AND c.abandonado = true AND c.fechaActualizacion >= :limite")
    Optional<CarritoEntity> findCarritoActivoPorCliente(@Param("clienteId") Long clienteId, @Param("limite") LocalDateTime limite);
}
