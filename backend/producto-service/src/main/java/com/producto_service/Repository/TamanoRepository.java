package com.producto_service.Repository;


import com.producto_service.Model.Tamano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TamanoRepository extends JpaRepository<Tamano, Integer> {

    List<Tamano> findByActivoTrue();

    Optional<Tamano> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}