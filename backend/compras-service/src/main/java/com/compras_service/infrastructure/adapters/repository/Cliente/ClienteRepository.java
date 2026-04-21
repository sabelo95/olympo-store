package com.compras_service.infrastructure.adapters.repository.Cliente;

import com.compras_service.infrastructure.adapters.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByNit(String nit);
    void deleteByNit(String nit);

    @Query("SELECT c FROM ClienteEntity c WHERE c.usuario_id = :usuarioId")
    Optional<ClienteEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query(value = "SELECT u.correo FROM usuario u JOIN cliente c ON c.usuario_id = u.id WHERE c.id = :id", nativeQuery = true)
    String findEmailById(@Param("id") Long id);
}

