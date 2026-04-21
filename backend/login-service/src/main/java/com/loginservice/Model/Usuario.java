package com.loginservice.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad Usuario que representa un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String correo;
    
    @Schema(description = "Contraseña encriptada del usuario", hidden = true)
    private String contrasena;
    
    @Schema(description = "Rol del usuario en el sistema", example = "ADMINISTRADOR", allowableValues = {"ADMINISTRADOR", "CLIENTE"})
    private String rol;
    
    @Schema(description = "Indica si el usuario está activo", example = "true")
    private boolean activo;


}
