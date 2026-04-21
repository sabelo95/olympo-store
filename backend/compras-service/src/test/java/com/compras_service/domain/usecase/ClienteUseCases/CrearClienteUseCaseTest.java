package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.UsuarioGateway;
import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearClienteUseCaseTest {

    @Mock
    private ClienteGateway clienteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CrearClienteUseCase crearClienteUseCase;

    private Cliente cliente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@test.com");
        usuario.setRol("CLIENTE");
        usuario.setActivo(true);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setNit("123456789");
        cliente.setCiudad("Bogota");
        cliente.setPais("Colombia");
        cliente.setUsuario_id(1L);
    }

    @Test
    void crearCliente_ValidCliente_CreatesCliente() {
        // Given
        when(usuarioGateway.obtenerUsuarioPorId(cliente.getUsuario_id()))
            .thenReturn(Optional.of(usuario));
        when(clienteGateway.obtenerClientePorNit(cliente.getNit()))
            .thenReturn(Optional.empty());
        when(clienteGateway.crearCliente(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente result = crearClienteUseCase.crearCliente(cliente);

        // Then
        assertNotNull(result);
        verify(clienteGateway).crearCliente(any(Cliente.class));
        verify(usuarioGateway).obtenerUsuarioPorId(cliente.getUsuario_id());
    }

    @Test
    void crearCliente_DuplicateNit_ThrowsException() {
        // Given
        when(usuarioGateway.obtenerUsuarioPorId(cliente.getUsuario_id()))
            .thenReturn(Optional.of(usuario));
        when(clienteGateway.obtenerClientePorNit(cliente.getNit()))
            .thenReturn(Optional.of(cliente));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            crearClienteUseCase.crearCliente(cliente));
    }

    @Test
    void crearCliente_InvalidUsuario_ThrowsException() {
        // Given
        when(usuarioGateway.obtenerUsuarioPorId(cliente.getUsuario_id()))
            .thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            crearClienteUseCase.crearCliente(cliente));
    }
}




