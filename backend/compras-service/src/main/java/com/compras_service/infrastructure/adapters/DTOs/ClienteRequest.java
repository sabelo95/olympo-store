package com.compras_service.infrastructure.adapters.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request para crear o actualizar un cliente")
public class ClienteRequest {


    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez", required = true)
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    @Schema(description = "Número de identificación tributaria (NIT)", example = "123456789", required = true)
    private String nit;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad de residencia del cliente", example = "Bogotá", required = true)
    private String ciudad;

    @NotBlank(message = "El país es obligatorio")
    @Schema(description = "País de residencia del cliente", example = "Colombia", required = true)
    private String pais;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del usuario asociado al cliente", example = "1", required = true)
    private Long usuarioId;
}
