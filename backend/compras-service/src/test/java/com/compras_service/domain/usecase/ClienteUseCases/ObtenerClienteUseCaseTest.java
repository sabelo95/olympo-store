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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerClienteUseCaseTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private ObtenerClienteUseCase obtenerClienteUseCase;

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
    void obtenerCliente_ExistingCliente_ReturnsCliente() {
        // Given
        String nit = "123456789";
        when(clienteGateway.obtenerClientePorNit(nit)).thenReturn(Optional.of(cliente));

        // When
        Cliente result = obtenerClienteUseCase.obtenerPorNit(nit);

        // Then
        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteGateway).obtenerClientePorNit(nit);
    }

    @Test
    void obtenerCliente_NonExistingCliente_ThrowsException() {
        // Given
        String nit = "999999999";
        when(clienteGateway.obtenerClientePorNit(nit)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            obtenerClienteUseCase.obtenerPorNit(nit));
        verify(clienteGateway).obtenerClientePorNit(nit);
    }
}




