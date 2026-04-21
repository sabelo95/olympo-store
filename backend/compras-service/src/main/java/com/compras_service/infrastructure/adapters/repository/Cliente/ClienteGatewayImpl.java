package com.compras_service.infrastructure.adapters.repository.Cliente;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.infrastructure.adapters.entity.ClienteEntity;
import com.compras_service.infrastructure.adapters.mapper.ClienteMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Transactional
public class
ClienteGatewayImpl implements ClienteGateway {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    @Override
    public Cliente crearCliente(Cliente cliente) {
        ClienteEntity clienteEntity = new ClienteMapper().toEntity(cliente);
        Cliente clieneSaved = clienteMapper.toDomain(clienteRepository.save(clienteEntity));
        return clieneSaved;
    }

    @Override
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDomain);
    }

    @Override

    public Optional<Cliente> actualizarCliente(Cliente cliente) {
        return clienteRepository.findByNit(cliente.getNit())
                .map(existingEntity -> {

                    existingEntity.setNombre(cliente.getNombre());
                    existingEntity.setCiudad(cliente.getCiudad());
                    existingEntity.setPais(cliente.getPais());
                    existingEntity.setUsuario_id(cliente.getUsuario_id());


                    ClienteEntity updated = clienteRepository.save(existingEntity);
                    return clienteMapper.toDomain(updated);
                });
    }

    @Override
    public Optional<Cliente> obtenerClientePorNit(String nit) {
        return clienteRepository.findByNit(nit)
                .map(clienteMapper::toDomain);
    }

    @Override
    public Optional<String> obtenerEmailPorId(double id) {
        return clienteRepository.findEmailById((long) id) != null ?
                Optional.ofNullable(clienteRepository.findEmailById((long) id))
                : Optional.empty();
    }

    @Override
    public Optional<Cliente> obtenerClientePorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId)
                .map(clienteMapper::toDomain);
    }


    @Override
    public void eliminarCliente(String nit) {
        if (clienteRepository.findByNit(nit).isPresent()) {
            clienteRepository.deleteByNit(nit);

        }
    }
}
