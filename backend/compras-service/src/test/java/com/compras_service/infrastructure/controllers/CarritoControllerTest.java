package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.usecase.CarritoUseCases.ActualizarCarritoUseCase;
import com.compras_service.domain.usecase.CarritoUseCases.CrearCarritoUseCase;
import com.compras_service.domain.usecase.CarritoUseCases.EliminarCarritoUseCase;
import com.compras_service.infrastructure.adapters.DTOs.CarritoProductoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CarritoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CarritoResponse;
import com.compras_service.infrastructure.adapters.mapper.web.CarritoWebMapper;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearCarritoUseCase crearCarritoUseCase;

    @MockBean
    private ActualizarCarritoUseCase actualizarCarritoUseCase;

    @MockBean
    private EliminarCarritoUseCase eliminarCarritoUseCase;

    @MockBean
    private CarritoWebMapper carritoWebMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CarritoRequest carritoRequest;
    private CarritoResponse carritoResponse;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        carritoRequest = new CarritoRequest();
        carritoRequest.setClienteId(1L);
        carritoRequest.setAbandonado(false);
        
        CarritoProductoRequest productoRequest = new CarritoProductoRequest();
        productoRequest.setProductoId(1L);
        productoRequest.setCantidad(2);
        List<CarritoProductoRequest> productos = new ArrayList<>();
        productos.add(productoRequest);
        carritoRequest.setProductos(productos);

        carritoResponse = new CarritoResponse();
        carritoResponse.setId(1L);
        carritoResponse.setClienteId(1L);
        carritoResponse.setAbandonado(false);

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCliente_id(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearCarrito_ValidRequest_ReturnsOk() throws Exception {
        // Given
        when(carritoWebMapper.toDomain(any(CarritoRequest.class))).thenReturn(carrito);
        when(crearCarritoUseCase.crearCarrito(any(Carrito.class))).thenReturn(carrito);
        when(carritoWebMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

        // When & Then
        mockMvc.perform(post("/api/carritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clienteId").value(1L));

        verify(crearCarritoUseCase, times(1)).crearCarrito(any(Carrito.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearCarrito_Exception_ReturnsInternalServerError() throws Exception {
        // Given
        when(carritoWebMapper.toDomain(any(CarritoRequest.class))).thenReturn(carrito);
        when(crearCarritoUseCase.crearCarrito(any(Carrito.class)))
                .thenThrow(new RuntimeException("Error al crear carrito"));

        // When & Then
        mockMvc.perform(post("/api/carritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoRequest)))
                .andExpect(status().isInternalServerError());

        verify(crearCarritoUseCase, times(1)).crearCarrito(any(Carrito.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarCarrito_ValidRequest_ReturnsOk() throws Exception {
        // Given
        Long carritoId = 1L;
        Long clienteId = 1L;
        when(carritoWebMapper.toDomain(any(CarritoRequest.class))).thenReturn(carrito);
        doNothing().when(actualizarCarritoUseCase).modificarCarrito(eq(carritoId), eq(clienteId), any(Carrito.class));

        // When & Then
        mockMvc.perform(put("/api/carritos/{id}/{clienteId}", carritoId, clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrito actualizado exitosamente."));

        verify(actualizarCarritoUseCase, times(1)).modificarCarrito(eq(carritoId), eq(clienteId), any(Carrito.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void actualizarCarrito_NonExistingCarrito_ReturnsNotFound() throws Exception {
        // Given
        Long carritoId = 999L;
        Long clienteId = 1L;
        when(carritoWebMapper.toDomain(any(CarritoRequest.class))).thenReturn(carrito);
        doThrow(new IllegalArgumentException("Carrito no encontrado"))
                .when(actualizarCarritoUseCase).modificarCarrito(eq(carritoId), eq(clienteId), any(Carrito.class));

        // When & Then
        mockMvc.perform(put("/api/carritos/{id}/{clienteId}", carritoId, clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Carrito no encontrado."));

        verify(actualizarCarritoUseCase, times(1)).modificarCarrito(eq(carritoId), eq(clienteId), any(Carrito.class));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarCarrito_ExistingId_ReturnsOk() throws Exception {
        // Given
        Long carritoId = 1L;
        doNothing().when(eliminarCarritoUseCase).eliminarCarrito(carritoId);

        // When & Then
        mockMvc.perform(delete("/api/carritos/{id}", carritoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrito eliminado exitosamente."));

        verify(eliminarCarritoUseCase, times(1)).eliminarCarrito(carritoId);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void eliminarCarrito_NonExistingId_ReturnsNotFound() throws Exception {
        // Given
        Long carritoId = 999L;
        doThrow(new IllegalArgumentException("Carrito no encontrado"))
                .when(eliminarCarritoUseCase).eliminarCarrito(carritoId);

        // When & Then
        mockMvc.perform(delete("/api/carritos/{id}", carritoId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Carrito no encontrado."));

        verify(eliminarCarritoUseCase, times(1)).eliminarCarrito(carritoId);
    }
}

