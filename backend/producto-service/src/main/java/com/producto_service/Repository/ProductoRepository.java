package com.producto_service.Repository;

import com.producto_service.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaNombre(String categoriaNombre);


    List<Producto> findByMarcaNombre(String marcaNombre);


    List<Producto> findAllById(Iterable<Long> ids);

    Optional<Producto> findById(Long id);

    Optional<Producto> findByNombre(String nombre);



}
