package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.usecase.PedidoUseCases.ActualizarPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.CrearPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.EliminarPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.ObtenerPedidoUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ActualizarPedidoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CrearPedidoRequest;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @MockBean
    private ObtenerPedidoUseCase obtenerPedidoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private ActualizarPedidoRequest actualizarPedidoRequest;
    private CrearPedidoRequest crearPedidoRequest;
    private Pedido pedidoResponse;

    @BeforeEach
    void setUp() {
        crearPedidoRequest = new CrearPedidoRequest();
        crearPedidoRequest.setDireccionEnvio("Calle 123 #45-67, Bogotá");
        crearPedidoRequest.setMetodoPago("EFECTIVO");

        actualizarPedidoRequest = new ActualizarPedidoRequest();
        actualizarPedidoRequest.setId(1L);
        actualizarPedidoRequest.setDireccionEnvio("Calle 123 #45-67, Bogotá");
        actualizarPedidoRequest.setDetalles(new ArrayList<>());

        pedidoResponse = new Pedido();
        pedidoResponse.setId(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearPedido_ValidRequest_ReturnsOk() throws Exception {
        Long carritoId = 1L;
        Long clienteId = 1L;
        when(crearPedidoUseCase.ejecutar(eq(clienteId), eq(carritoId), any(), any())).thenReturn(pedidoResponse);

        mockMvc.perform(post("/api/pedidos/carrito/{id}/cliente/{clienteId}", carritoId, clienteId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearPedidoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pedido creado con éxito")));

        verify(crearPedidoUseCase, times(1)).ejecutar(eq(clienteId), eq(carritoId), any(), any());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearPedido_InvalidRequest_ReturnsNotFound() throws Exception {
        Long carritoId = 999L;
        Long clienteId = 999L;
        doThrow(new IllegalArgumentException("Carrito o cliente no encontrado"))
                .when(crearPedidoUseCase).ejecutar(eq(clienteId), eq(carritoId), any(), any());

        mockMvc.perform(post("/api/pedidos/carrito/{id}/cliente/{clienteId}", carritoId, clienteId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearPedidoRequest)))
                .andExpect(status().isNotFound());

        verify(crearPedidoUseCase, times(1)).ejecutar(eq(clienteId), eq(carritoId), any(), any());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarPedido_ValidRequest_ReturnsOk() throws Exception {
        // actualizarPedido returns Pedido — use when().thenReturn(), not doNothing()
        when(actualizarPedidoUseCase.actualizarPedido(any(ActualizarPedidoRequest.class)))
                .thenReturn(pedidoResponse);

        mockMvc.perform(put("/api/pedidos/actualizar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPedidoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido actualizado exitosamente."));

        verify(actualizarPedidoUseCase, times(1)).actualizarPedido(any(ActualizarPedidoRequest.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarPedido_NonExistingPedido_ReturnsBadRequest() throws Exception {
        when(actualizarPedidoUseCase.actualizarPedido(any(ActualizarPedidoRequest.class)))
                .thenThrow(new IllegalArgumentException("Pedido no encontrado"));

        mockMvc.perform(put("/api/pedidos/actualizar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPedidoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pedido no encontrado"));

        verify(actualizarPedidoUseCase, times(1)).actualizarPedido(any(ActualizarPedidoRequest.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void cambiarEstadoPedido_ValidRequest_ReturnsOk() throws Exception {
        Long pedidoId = 1L;
        // Use a valid EstadoPedido value: CREADO, EN_PROCESO, COMPLETADO, CANCELADO
        String estado = "EN_PROCESO";
        // actualizarEstadoPedido returns Pedido — use when().thenReturn()
        when(actualizarPedidoUseCase.actualizarEstadoPedido(eq(pedidoId), eq(EstadoPedido.EN_PROCESO)))
                .thenReturn(pedidoResponse);

        mockMvc.perform(put("/api/pedidos/actualizar-estado")
                        .with(csrf())
                        .param("id", String.valueOf(pedidoId))
                        .param("estado", estado))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado del pedido actualizado exitosamente."));

        verify(actualizarPedidoUseCase, times(1))
                .actualizarEstadoPedido(eq(pedidoId), eq(EstadoPedido.EN_PROCESO));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void cambiarEstadoPedido_NonExistingPedido_ReturnsNotFound() throws Exception {
        Long pedidoId = 999L;
        // Use a valid EstadoPedido value
        String estado = "EN_PROCESO";
        when(actualizarPedidoUseCase.actualizarEstadoPedido(eq(pedidoId), eq(EstadoPedido.EN_PROCESO)))
                .thenThrow(new IllegalArgumentException("Pedido no encontrado"));

        mockMvc.perform(put("/api/pedidos/actualizar-estado")
                        .with(csrf())
                        .param("id", String.valueOf(pedidoId))
                        .param("estado", estado))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido no encontrado"));

        verify(actualizarPedidoUseCase, times(1))
                .actualizarEstadoPedido(eq(pedidoId), eq(EstadoPedido.EN_PROCESO));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarPedido_ExistingId_ReturnsOk() throws Exception {
        Long pedidoId = 1L;
        doNothing().when(eliminarPedidoUseCase).eliminarPedido(pedidoId);

        mockMvc.perform(delete("/api/pedidos")
                        .with(csrf())
                        .param("id", String.valueOf(pedidoId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido eliminado exitosamente."));

        verify(eliminarPedidoUseCase, times(1)).eliminarPedido(pedidoId);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarPedido_NonExistingId_ReturnsNotFound() throws Exception {
        Long pedidoId = 999L;
        doThrow(new IllegalArgumentException("Pedido no encontrado"))
                .when(eliminarPedidoUseCase).eliminarPedido(pedidoId);

        mockMvc.perform(delete("/api/pedidos")
                        .with(csrf())
                        .param("id", String.valueOf(pedidoId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido no encontrado."));

        verify(eliminarPedidoUseCase, times(1)).eliminarPedido(pedidoId);
    }
}
