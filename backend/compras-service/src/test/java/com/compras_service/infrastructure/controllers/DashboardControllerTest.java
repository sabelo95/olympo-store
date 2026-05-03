package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.ProductoVenta;
import com.compras_service.domain.usecase.DashboardUseCases.ObtenerDashboardUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObtenerDashboardUseCase obtenerDashboardUseCase;

    @BeforeEach
    void setUp() {
        when(obtenerDashboardUseCase.obtenerTotalPedidos()).thenReturn(42L);
        when(obtenerDashboardUseCase.obtenerTotalIngresos()).thenReturn(5_800_000.0);
        when(obtenerDashboardUseCase.obtenerPedidosPorEstado()).thenReturn(
                Map.of("PENDIENTE", 10L, "EN_PROCESO", 8L, "ENVIADO", 15L, "ENTREGADO", 9L)
        );
        when(obtenerDashboardUseCase.obtenerTotalClientes()).thenReturn(150L);
    }

    // ── RESUMEN ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void obtenerResumen_conRolAdmin_retornaKPIs() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/dashboard/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPedidos").value(42))
                .andExpect(jsonPath("$.totalIngresos").value(5800000.0))
                .andExpect(jsonPath("$.totalClientes").value(150));

        verify(obtenerDashboardUseCase).obtenerTotalPedidos();
        verify(obtenerDashboardUseCase).obtenerTotalIngresos();
        verify(obtenerDashboardUseCase).obtenerTotalClientes();
    }

    // ── PEDIDOS RECIENTES ──────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void obtenerPedidosRecientes_conRolAdmin_retornaLista() throws Exception {
        // Given
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(obtenerDashboardUseCase.obtenerPedidosRecientes(10)).thenReturn(List.of(pedido));

        // When & Then
        mockMvc.perform(get("/api/dashboard/pedidos-recientes").param("limite", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(obtenerDashboardUseCase).obtenerPedidosRecientes(10);
    }

    // ── TOP PRODUCTOS ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void topProductos_retornaListaOrdenada() throws Exception {
        // Given
        ProductoVenta p = new ProductoVenta();
        p.setProductoId(1L);
        p.setTotalVendido(120L);
        when(obtenerDashboardUseCase.obtenerTopProductos(5)).thenReturn(List.of(p));

        // When & Then
        mockMvc.perform(get("/api/dashboard/top-productos").param("limite", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(1L))
                .andExpect(jsonPath("$[0].totalVendido").value(120L));
    }

    // ── VENTAS POR PERÍODO ─────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void ventasPorPeriodo_fechasValidas_retornaDTO() throws Exception {
        // Given
        when(obtenerDashboardUseCase.obtenerPedidosPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(new Pedido()));
        when(obtenerDashboardUseCase.obtenerIngresosEnPeriodo(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(1_200_000.0);

        // When & Then
        mockMvc.perform(get("/api/dashboard/ventas-por-periodo")
                        .param("fechaInicio", "2024-01-01T00:00:00")
                        .param("fechaFin", "2024-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPedidos").value(1))
                .andExpect(jsonPath("$.totalIngresos").value(1200000.0));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void ventasPorPeriodo_formatoFechaInvalido_retorna500() throws Exception {
        // When & Then — LocalDateTime.parse throws exception in controller -> 500
        mockMvc.perform(get("/api/dashboard/ventas-por-periodo")
                        .param("fechaInicio", "01/01/2024")
                        .param("fechaFin", "31/12/2024"))
                .andExpect(status().isInternalServerError());
    }
}
