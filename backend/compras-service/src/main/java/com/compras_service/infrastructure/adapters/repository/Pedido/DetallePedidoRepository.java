package com.compras_service.infrastructure.adapters.repository.Pedido;

import com.compras_service.infrastructure.adapters.entity.DetallePedidoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {

    @Query("SELECT d.producto_id, SUM(d.cantidad) FROM DetallePedidoEntity d GROUP BY d.producto_id ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> findTopProductos(Pageable pageable);
}
