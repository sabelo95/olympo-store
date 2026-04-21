package com.compras_service.infrastructure.adapters.repository.Usuario;

import com.compras_service.domain.model.Usuario;
import com.compras_service.domain.gateways.UsuarioGateway;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;
    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        try {
            ResponseEntity<Usuario> response = restTemplate.getForEntity(
                    "http://localhost:8082/auth/obtener/" + id,
                    Usuario.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }




}
