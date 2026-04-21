package com.producto_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionRequestDto {
    private String tipo;
    private String destinatario;
    private String mensaje;
}
