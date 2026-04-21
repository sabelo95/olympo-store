package com.compras_service.infrastructure.adapters.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Request para crear o actualizar un carrito")
public class CarritoRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente propietario del carrito", example = "1", required = true)
    private Long clienteId;

    @NotNull(message = "El estado 'abandonado' es obligatorio")
    @Schema(description = "Indica si el carrito está abandonado", example = "false", required = true)
    private Boolean abandonado = true;

    @NotEmpty(message = "Debe incluir al menos un producto en el carrito")
    @Schema(description = "Lista de productos en el carrito", required = true)
    private List<@Valid CarritoProductoRequest> productos;
}
