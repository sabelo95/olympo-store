package com.producto_service.Controller;

import com.producto_service.Config.JwtUtil;
import com.producto_service.Service.NotificacionService;
import com.producto_service.Service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reporte")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "API para generación de reportes")
public class ReporteController {

    private final ReporteService reporteService;
    private final NotificacionService notificacionService;
    private final JwtUtil jwtUtil;

    @GetMapping("/inventario-bajo")
    @Operation(summary = "Generar reporte de inventario bajo", description = "Genera un reporte PDF de productos con inventario bajo y lo envía por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<byte[]> generarReporte(
            @Parameter(description = "Límite de stock para considerar inventario bajo", required = true, example = "10")
            @RequestParam int limite) {
        String correoUsuario = jwtUtil.obtenerCorreoActual();

        try {
            byte[] pdf = reporteService.generarReporteInventarioBajo(limite);
            notificacionService.enviarNotificacionConAdjunto(
                    "EMAIL",
                    correoUsuario,
                    "Se ha generado el reporte de inventario bajo con límite: " + limite,
                    pdf,
                    "reporte_inventario_bajo.pdf"
            );
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_inventario_bajo.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}
