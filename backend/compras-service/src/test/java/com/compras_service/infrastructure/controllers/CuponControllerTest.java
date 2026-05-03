package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Cupon;
import com.compras_service.domain.usecase.CuponUseCases.ActualizarCuponUseCase;
import com.compras_service.domain.usecase.CuponUseCases.CrearCuponUseCase;
import com.compras_service.domain.usecase.CuponUseCases.EliminarCuponUseCase;
import com.compras_service.domain.usecase.CuponUseCases.ObtenerCuponUseCase;
import com.compras_service.domain.usecase.CuponUseCases.ValidarCuponUseCase;
import com.compras_service.infrastructure.adapters.DTOs.CuponRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuponController.class)
class CuponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidarCuponUseCase validarCuponUseCase;

    @MockBean
    private CrearCuponUseCase crearCuponUseCase;

    @MockBean
    private ObtenerCuponUseCase obtenerCuponUseCase;

    @MockBean
    private ActualizarCuponUseCase actualizarCuponUseCase;

    @MockBean
    private EliminarCuponUseCase eliminarCuponUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Cupon cuponValido;
    private CuponRequest cuponRequest;

    @BeforeEach
    void setUp() {
        cuponValido = new Cupon();
        cuponValido.setId(1L);
        cuponValido.setCodigo("DESCUENTO10");
        cuponValido.setDescuentoPorcentaje(10);
        cuponValido.setActivo(true);
        cuponValido.setUsoMaximo(100);
        cuponValido.setUsoActual(5);
        cuponValido.setMontoMinimo(50000L);
        cuponValido.setFechaExpiracion(LocalDate.now().plusDays(30));

        cuponRequest = new CuponRequest();
        cuponRequest.setCodigo("DESCUENTO10");
        cuponRequest.setDescuentoPorcentaje(10);
        cuponRequest.setActivo(true);
        cuponRequest.setUsoMaximo(100);
        cuponRequest.setMontoMinimo(50000L);
        cuponRequest.setFechaExpiracion(LocalDate.now().plusDays(30));
    }

    // ── VALIDAR CUPÓN ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void validarCupon_codigoValido_retorna200ConDescuento() throws Exception {
        // Given
        when(validarCuponUseCase.validar(eq("DESCUENTO10"), eq(200000L))).thenReturn(cuponValido);

        // When & Then
        mockMvc.perform(get("/api/cupones/validar/DESCUENTO10")
                        .param("total", "200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("DESCUENTO10"))
                .andExpect(jsonPath("$.descuentoPorcentaje").value(10));

        verify(validarCuponUseCase).validar("DESCUENTO10", 200000L);
    }

    @Test
    @WithMockUser
    void validarCupon_codigoExpirado_retorna400() throws Exception {
        // Given
        when(validarCuponUseCase.validar(eq("VENCIDO"), anyLong()))
                .thenThrow(new IllegalArgumentException("El cupón ha expirado"));

        // When & Then
        mockMvc.perform(get("/api/cupones/validar/VENCIDO")
                        .param("total", "200000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El cupón ha expirado"));
    }

    @Test
    @WithMockUser
    void validarCupon_montoMenorAlMinimo_retorna400() throws Exception {
        // Given
        when(validarCuponUseCase.validar(eq("DESCUENTO10"), eq(10000L)))
                .thenThrow(new IllegalArgumentException("El monto mínimo para este cupón es $50.000"));

        // When & Then
        mockMvc.perform(get("/api/cupones/validar/DESCUENTO10")
                        .param("total", "10000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void validarCupon_usoMaximoAlcanzado_retorna400() throws Exception {
        // Given
        when(validarCuponUseCase.validar(eq("AGOTADO"), anyLong()))
                .thenThrow(new IllegalArgumentException("El cupón ha alcanzado su límite de usos"));

        // When & Then
        mockMvc.perform(get("/api/cupones/validar/AGOTADO")
                        .param("total", "200000"))
                .andExpect(status().isBadRequest());
    }

    // ── CREAR CUPÓN ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void crearCupon_datosValidos_retorna201() throws Exception {
        // Given
        when(crearCuponUseCase.crear(any(Cupon.class))).thenReturn(cuponValido);

        // When & Then
        mockMvc.perform(post("/api/cupones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuponRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("DESCUENTO10"));

        verify(crearCuponUseCase).crear(any(Cupon.class));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void crearCupon_codigoDuplicado_retorna400() throws Exception {
        // Given
        when(crearCuponUseCase.crear(any(Cupon.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un cupón con ese código"));

        // When & Then
        mockMvc.perform(post("/api/cupones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuponRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe un cupón con ese código"));
    }

    // ── LISTAR CUPONES ─────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void listarCupones_conRolAdmin_retornaLista() throws Exception {
        // Given
        when(obtenerCuponUseCase.obtenerTodos()).thenReturn(List.of(cuponValido));

        // When & Then
        mockMvc.perform(get("/api/cupones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("DESCUENTO10"));
    }

    // ── ELIMINAR CUPÓN ─────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void eliminarCupon_codigoExistente_retorna200() throws Exception {
        // Given
        doNothing().when(eliminarCuponUseCase).eliminar("DESCUENTO10");

        // When & Then
        mockMvc.perform(delete("/api/cupones/DESCUENTO10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Cupón eliminado exitosamente."));

        verify(eliminarCuponUseCase).eliminar("DESCUENTO10");
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void eliminarCupon_codigoInexistente_retorna404() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("Cupón no encontrado"))
                .when(eliminarCuponUseCase).eliminar("INEXISTENTE");

        // When & Then
        mockMvc.perform(delete("/api/cupones/INEXISTENTE")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cupón no encontrado"));
    }
}
