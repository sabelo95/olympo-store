package com.loginservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta al crear usuario")
public class UsuarioResponse {

    @Schema(description = "ID del usuario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Schema(description = "Rol del usuario", example = "CLIENTE", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rol;

    @Schema(description = "Estado del usuario", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean activo;
}
