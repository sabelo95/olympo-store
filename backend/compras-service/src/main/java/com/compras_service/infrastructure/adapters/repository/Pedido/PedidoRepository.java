package com.compras_service.infrastructure.adapters.repository.Pedido;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.infrastructure.adapters.entity.PedidoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    Optional<List<PedidoEntity>> findByEstado(EstadoPedido estado);

    List<PedidoEntity> findByClienteId(Long id);

    long countByEstado(EstadoPedido estado);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM PedidoEntity p WHERE p.estado = 'COMPLETADO'")
    Double sumTotalCompletados();

    List<PedidoEntity> findTopByOrderByFechaPedidoDesc(Pageable pageable);

    @Query("SELECT p FROM PedidoEntity p WHERE p.fechaPedido BETWEEN :inicio AND :fin ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByFechaPedidoBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM PedidoEntity p WHERE p.estado = 'COMPLETADO' AND p.fechaPedido BETWEEN :inicio AND :fin")
    Double sumTotalCompletadosEnPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
