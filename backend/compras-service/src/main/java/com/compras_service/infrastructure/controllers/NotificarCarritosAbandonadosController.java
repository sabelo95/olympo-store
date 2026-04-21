package com.compras_service.infrastructure.controllers;


import com.compras_service.domain.usecase.NotificacionesUseCases.NotificarCarritosAbandonadosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carritos")
@RequiredArgsConstructor
public class NotificarCarritosAbandonadosController {

    private final NotificarCarritosAbandonadosUseCase notificarCarritosAbandonadosUseCase;

    /**
     * Endpoint de prueba para ejecutar manualmente la notificación
     * de carritos abandonados.
     *
     * Ejemplo: GET http://localhost:8080/carritos/notificar-abandonados
     */
    @GetMapping("/notificar-abandonados")
    public ResponseEntity<String> notificarCarritosAbandonados() {
        try {
            notificarCarritosAbandonadosUseCase.notificarCarritosAbandonados();
            return ResponseEntity.ok("✅ Proceso de notificación de carritos abandonados ejecutado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("❌ Error al ejecutar el proceso: " + e.getMessage());
        }
    }
}
