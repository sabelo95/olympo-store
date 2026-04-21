package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.usecase.ReportesUseCases.GenerarReportesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "API para generación de reportes")
public class ReporteController {

    private final GenerarReportesUseCase generarReporteSemanalUseCase;

    @GetMapping("/ventas-semanal")
    @Operation(summary = "Generar reporte de ventas", description = "Genera un reporte PDF de ventas entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> generarReporte(
            @Parameter(description = "Fecha de inicio en formato YYYY-MM-DD", required = true, example = "2024-01-01")
            @RequestParam String fechaInicio,
            @Parameter(description = "Fecha de fin en formato YYYY-MM-DD", required = true, example = "2024-01-31")
            @RequestParam String fechaFin) {
        try {
            byte[] pdf = generarReporteSemanalUseCase.ejecutar(fechaInicio, fechaFin);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ventas_semanal.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el reporte: " + e.getMessage());
        }
    }
}

