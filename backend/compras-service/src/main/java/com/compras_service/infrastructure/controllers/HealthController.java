package com.compras_service.infrastructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/health")
@Tag(name = "Health", description = "Verificación del estado del servicio de Compras")
public class HealthController {

    @Operation(
            summary = "Verifica el estado del servicio",
            description = "Devuelve un mensaje indicando si el servicio de Compras está funcionando correctamente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "El servicio está funcionando correctamente")
            }
    )
    @GetMapping
    public String healthCheck() {
        return "Compras Service está funcionando";
    }
}

