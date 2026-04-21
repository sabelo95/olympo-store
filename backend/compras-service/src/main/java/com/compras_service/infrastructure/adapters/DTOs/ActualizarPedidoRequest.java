package com.compras_service.infrastructure.adapters.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Request para actualizar un pedido existente")
public class ActualizarPedidoRequest {

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    @Schema(description = "ID del pedido a actualizar", example = "1", required = true)
    private Long id;

    @NotBlank(message = "La dirección de envío no puede estar vacía")
    @Size(max = 255, message = "La dirección de envío no puede tener más de 255 caracteres")
    @Schema(description = "Dirección de envío del pedido", example = "Calle 123 #45-67, Bogotá", required = true)
    private String direccionEnvio;

    @NotNull(message = "Debe incluir al menos un detalle de pedido")
    @Size(min = 1, message = "Debe haber al menos un detalle de pedido")
    @Schema(description = "Lista de productos y cantidades del pedido", required = true)
    private List<@Valid DetallePedidoRequest> detalles;
}


