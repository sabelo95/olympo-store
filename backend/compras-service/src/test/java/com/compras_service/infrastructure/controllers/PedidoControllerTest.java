package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.usecase.PedidoUseCases.ActualizarPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.CrearPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.EliminarPedidoUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ActualizarPedidoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearPedidoUseCase crearPedidoUseCase;

    @MockBean
    private ActualizarPedidoUseCase actualizarPedidoUseCase;

    @MockBean
    private EliminarPedidoUseCase eliminarPedidoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private ActualizarPedidoRequest actualizarPedidoRequest;

    @BeforeEach
    void setUp() {
        actualizarPedidoRequest = new ActualizarPedidoRequest();
        actualizarPedidoRequest.setId(1L);
        actualizarPedidoRequest.setDireccionEnvio("Calle 123 #45-67, Bogotá");
        actualizarPedidoRequest.setDetallePedido(new ArrayList<>());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearPedido_ValidRequest_ReturnsOk() throws Exception {
        // Given
        Long carritoId = 1L;
        Long clienteId = 1L;
        String direccion = "Calle 123 #45-67, Bogotá";
        doNothing().when(crearPedidoUseCase).crearPedido(clienteId, carritoId, direccion);

        // When & Then
        mockMvc.perform(post("/api/pedidos/carrito/{id}/cliente/{clienteId}", carritoId, clienteId))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido creado con éxito"));

        verify(crearPedidoUseCase, times(1)).crearPedido(clienteId, carritoId, direccion);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearPedido_InvalidRequest_ReturnsNotFound() throws Exception {
        // Given
        Long carritoId = 999L;
        Long clienteId = 999L;
        String d = "Calle 123 #45-67, Bogotá";
        doThrow(new IllegalArgumentException("Carrito o cliente no encontrado"))
                .when(crearPedidoUseCase).crearPedido(clienteId, carritoId,d);

        // When & Then
        mockMvc.perform(post("/api/pedidos/carrito/{id}/cliente/{clienteId}", carritoId, clienteId))
                .andExpect(status().isNotFound());

        verify(crearPedidoUseCase, times(1)).crearPedido(clienteId, carritoId,d);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarPedido_ValidRequest_ReturnsOk() throws Exception {
        // Given
        doNothing().when(actualizarPedidoUseCase).actualizarPedido(any(ActualizarPedidoRequest.class));

        // When & Then
        mockMvc.perform(put("/api/pedidos/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPedidoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido actualizado exitosamente."));

        verify(actualizarPedidoUseCase, times(1)).actualizarPedido(any(ActualizarPedidoRequest.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarPedido_NonExistingPedido_ReturnsNotFound() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("Pedido no encontrado"))
                .when(actualizarPedidoUseCase).actualizarPedido(any(ActualizarPedidoRequest.class));

        // When & Then
        mockMvc.perform(put("/api/pedidos/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPedidoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido no encontrado."));

        verify(actualizarPedidoUseCase, times(1)).actualizarPedido(any(ActualizarPedidoRequest.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void cambiarEstadoPedido_ValidRequest_ReturnsOk() throws Exception {
        // Given
        Long pedidoId = 1L;
        String estado = "CONFIRMADO";
        doNothing().when(actualizarPedidoUseCase).actualizarEstadoPedido(eq(pedidoId), any());

        // When & Then
        mockMvc.perform(put("/api/pedidos/actualizar-estado")
                        .param("id", String.valueOf(pedidoId))
                        .param("estado", estado))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado del pedido actualizado exitosamente."));

        verify(actualizarPedidoUseCase, times(1)).actualizarEstadoPedido(eq(pedidoId), any());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void cambiarEstadoPedido_NonExistingPedido_ReturnsNotFound() throws Exception {
        // Given
        Long pedidoId = 999L;
        String estado = "CONFIRMADO";
        doThrow(new IllegalArgumentException("Pedido no encontrado"))
                .when(actualizarPedidoUseCase).actualizarEstadoPedido(eq(pedidoId), any());

        // When & Then
        mockMvc.perform(put("/api/pedidos/actualizar-estado")
                        .param("id", String.valueOf(pedidoId))
                        .param("estado", estado))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido no encontrado."));

        verify(actualizarPedidoUseCase, times(1)).actualizarEstadoPedido(eq(pedidoId), any());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarPedido_ExistingId_ReturnsOk() throws Exception {
        // Given
        Long pedidoId = 1L;
        doNothing().when(eliminarPedidoUseCase).eliminarPedido(pedidoId);

        // When & Then
        mockMvc.perform(delete("/api/pedidos")
                        .param("id", String.valueOf(pedidoId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido eliminado exitosamente."));

        verify(eliminarPedidoUseCase, times(1)).eliminarPedido(pedidoId);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarPedido_NonExistingId_ReturnsNotFound() throws Exception {
        // Given
        Long pedidoId = 999L;
        doThrow(new IllegalArgumentException("Pedido no encontrado"))
                .when(eliminarPedidoUseCase).eliminarPedido(pedidoId);

        // When & Then
        mockMvc.perform(delete("/api/pedidos")
                        .param("id", String.valueOf(pedidoId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido no encontrado."));

        verify(eliminarPedidoUseCase, times(1)).eliminarPedido(pedidoId);
    }
}

