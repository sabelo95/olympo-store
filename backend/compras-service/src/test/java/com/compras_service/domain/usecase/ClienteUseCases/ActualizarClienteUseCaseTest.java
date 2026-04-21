package com.compras_service.domain.usecase.ClienteUseCases;

import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.model.Cliente;
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
class ActualizarClienteUseCaseTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private ActualizarClienteUseCase actualizarClienteUseCase;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setNit("123456789");
        cliente.setCiudad("Bogota");
        cliente.setPais("Colombia");
    }

    @Test
    void actualizarCliente_ExistingCliente_UpdatesCliente() {
        // Given
        when(clienteGateway.actualizarCliente(any(Cliente.class))).thenReturn(Optional.of(cliente));

        // When
        Cliente result = actualizarClienteUseCase.actualizarCliente(cliente);

        // Then
        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteGateway).actualizarCliente(any(Cliente.class));
    }

    @Test
    void actualizarCliente_NonExistingCliente_ThrowsException() {
        // Given
        cliente.setId(999L);
        when(clienteGateway.actualizarCliente(any(Cliente.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            actualizarClienteUseCase.actualizarCliente(cliente));
        verify(clienteGateway).actualizarCliente(any(Cliente.class));
    }
}




