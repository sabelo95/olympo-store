package com.producto_service.Repository;

import com.producto_service.Model.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {

    // ─── BÚSQUEDAS POR PRODUCTO ───────────────────────────────────────────────
    List<VarianteProducto> findByProductoId(Long idProducto);




}

