package com.Notificaciones_service.Modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad Notificacion que representa una notificación enviada")
public class Notificacion {
    @Id
    @Schema(description = "ID único de la notificación", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Tipo de notificación", example = "EMAIL")
    private String tipo;
    
    @Schema(description = "Correo electrónico del destinatario", example = "usuario@example.com")
    private String destinatario;
    
    @Schema(description = "Mensaje de la notificación", example = "Notificación importante")
    private String mensaje;
    
    @Schema(description = "Fecha y hora en que se envió la notificación", example = "2025-11-02T10:30:00")
    private LocalDateTime fecha;
}
