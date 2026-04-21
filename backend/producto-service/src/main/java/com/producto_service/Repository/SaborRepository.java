package com.producto_service.Repository;

import com.producto_service.Model.Sabor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaborRepository extends JpaRepository<Sabor, Integer> {

    List<Sabor> findByActivoTrue();

    Optional<Sabor> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
