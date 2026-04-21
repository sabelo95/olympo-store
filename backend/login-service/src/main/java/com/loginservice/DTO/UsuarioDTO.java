package com.loginservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para autenticación de usuario")
public class UsuarioDTO {
    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com", required = true)
    private String correo;
    
    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    private String contrasena;

    public UsuarioDTO() {
    }
public UsuarioDTO(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
