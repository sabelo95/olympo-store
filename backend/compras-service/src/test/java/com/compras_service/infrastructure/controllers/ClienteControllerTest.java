package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.usecase.ClienteUseCases.ActualizarClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.CrearClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.EliminarClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.ObtenerClienteUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ClienteRequest;
import com.compras_service.infrastructure.adapters.DTOs.ClienteResponse;
import com.compras_service.infrastructure.adapters.mapper.web.ClienteWebMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearClienteUseCase crearClienteUseCase;

    @MockBean
    private ObtenerClienteUseCase obtenerClienteUseCase;

    @MockBean
    private ActualizarClienteUseCase actualizarClienteUseCase;

    @MockBean
    private EliminarClienteUseCase eliminarClienteUseCase;

    @MockBean
    private ClienteWebMapper clienteWebMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private ClienteRequest clienteRequest;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setNit("123456789");
        cliente.setCiudad("Bogotá");
        cliente.setPais("Colombia");
        cliente.setUsuario_id(1L);

        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan Pérez");
        clienteRequest.setNit("123456789");
        clienteRequest.setCiudad("Bogotá");
        clienteRequest.setPais("Colombia");
        clienteRequest.setUsuarioId(1L);

        clienteResponse = new ClienteResponse();
        clienteResponse.setId(1L);
        clienteResponse.setNombre("Juan Pérez");
        clienteResponse.setNit("123456789");
        clienteResponse.setCiudad("Bogotá");
        clienteResponse.setPais("Colombia");
        clienteResponse.setUsuarioId(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearCliente_ValidRequest_ReturnsOk() throws Exception {
        // Given
        when(clienteWebMapper.toDomain(any(ClienteRequest.class))).thenReturn(cliente);
        when(crearClienteUseCase.crearCliente(any(Cliente.class))).thenReturn(cliente);
        when(clienteWebMapper.toResponse(any(Cliente.class))).thenReturn(clienteResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.nit").value("123456789"));

        verify(crearClienteUseCase, times(1)).crearCliente(any(Cliente.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearCliente_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Given
        when(clienteWebMapper.toDomain(any(ClienteRequest.class))).thenReturn(cliente);
        when(crearClienteUseCase.crearCliente(any(Cliente.class)))
                .thenThrow(new IllegalArgumentException("NIT duplicado"));

        // When & Then
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isBadRequest());

        verify(crearClienteUseCase, times(1)).crearCliente(any(Cliente.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void obtenerCliente_ExistingNit_ReturnsOk() throws Exception {
        // Given
        String nit = "123456789";
        when(obtenerClienteUseCase.obtenerPorNit(nit)).thenReturn(cliente);
        when(clienteWebMapper.toResponse(any(Cliente.class))).thenReturn(clienteResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/clientes/{nit}", nit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nit").value("123456789"));

        verify(obtenerClienteUseCase, times(1)).obtenerPorNit(nit);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void obtenerCliente_NonExistingNit_ReturnsNotFound() throws Exception {
        // Given
        String nit = "999999999";
        when(obtenerClienteUseCase.obtenerPorNit(nit))
                .thenThrow(new IllegalArgumentException("Cliente no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/v1/clientes/{nit}", nit))
                .andExpect(status().isNotFound());

        verify(obtenerClienteUseCase, times(1)).obtenerPorNit(nit);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarCliente_ValidRequest_ReturnsOk() throws Exception {
        // Given
        String nit = "123456789";
        when(clienteWebMapper.toDomain(any(ClienteRequest.class))).thenReturn(cliente);
        doNothing().when(actualizarClienteUseCase).actualizarCliente(any(Cliente.class));

        // When & Then
        mockMvc.perform(put("/api/v1/clientes/{nit}", nit)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente actualizado exitosamente."));

        verify(actualizarClienteUseCase, times(1)).actualizarCliente(any(Cliente.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarCliente_NonExistingCliente_ReturnsNotFound() throws Exception {
        // Given
        String nit = "999999999";
        when(clienteWebMapper.toDomain(any(ClienteRequest.class))).thenReturn(cliente);
        doThrow(new IllegalArgumentException("Cliente no encontrado"))
                .when(actualizarClienteUseCase).actualizarCliente(any(Cliente.class));

        // When & Then
        mockMvc.perform(put("/api/v1/clientes/{nit}", nit)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado."));

        verify(actualizarClienteUseCase, times(1)).actualizarCliente(any(Cliente.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarCliente_ExistingNit_ReturnsOk() throws Exception {
        // Given
        String nit = "123456789";
        doNothing().when(eliminarClienteUseCase).eliminarCliente(nit);

        // When & Then
        mockMvc.perform(delete("/api/v1/clientes/{nit}", nit))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente eliminado exitosamente."));

        verify(eliminarClienteUseCase, times(1)).eliminarCliente(nit);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarCliente_NonExistingNit_ReturnsNotFound() throws Exception {
        // Given
        String nit = "999999999";
        doThrow(new IllegalArgumentException("Cliente no encontrado"))
                .when(eliminarClienteUseCase).eliminarCliente(nit);

        // When & Then
        mockMvc.perform(delete("/api/v1/clientes/{nit}", nit))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado."));

        verify(eliminarClienteUseCase, times(1)).eliminarCliente(nit);
    }
}

