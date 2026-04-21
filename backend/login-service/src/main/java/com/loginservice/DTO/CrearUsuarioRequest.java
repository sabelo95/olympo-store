package com.loginservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo usuario")
public class CrearUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Pérez",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Schema(
            description = "Correo electrónico del usuario",
            example = "usuario@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(
            description = "Contraseña del usuario",
            example = "password123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(
            description = "Rol del usuario",
            example = "CLIENTE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"ADMINISTRADOR", "CLIENTE"}
    )
    private String rol;
}
