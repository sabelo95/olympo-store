package com.Notificaciones_service.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "Request para enviar una notificación")
public class NotificacionRequest {
    @Schema(description = "Tipo de notificación", example = "EMAIL", required = true)
    private String tipo;
    
    @Schema(description = "Correo electrónico del destinatario", example = "usuario@example.com", required = true)
    private String destinatario;
    
    @Schema(description = "Mensaje de la notificación", example = "Este es un mensaje de prueba", required = true)
    private String mensaje;
    
    @Schema(description = "Archivo adjunto (opcional)", required = false)
    private MultipartFile archivo;
}

