package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.usecase.ReportesUseCases.GenerarReportesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerarReportesUseCase generarReportesUseCase;

    @Test
    @WithMockUser(roles = "ADMIN")
    void generarReporte_ValidDates_ReturnsPdf() throws Exception {
        // Given
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        byte[] pdfContent = "PDF Content".getBytes();
        when(generarReportesUseCase.ejecutar(fechaInicio, fechaFin)).thenReturn(pdfContent);

        // When & Then
        mockMvc.perform(get("/reportes/ventas-semanal")
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=reporte_ventas_semanal.pdf"))
                .andExpect(content().contentType("application/pdf"));

        verify(generarReportesUseCase, times(1)).ejecutar(fechaInicio, fechaFin);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generarReporte_Exception_ReturnsInternalServerError() throws Exception {
        // Given
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        when(generarReportesUseCase.ejecutar(anyString(), anyString()))
                .thenThrow(new RuntimeException("Error al generar reporte"));

        // When & Then
        mockMvc.perform(get("/reportes/ventas-semanal")
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isInternalServerError());

        verify(generarReportesUseCase, times(1)).ejecutar(fechaInicio, fechaFin);
    }
}

