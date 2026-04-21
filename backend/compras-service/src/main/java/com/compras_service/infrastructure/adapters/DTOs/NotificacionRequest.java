package com.compras_service.infrastructure.adapters.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionRequest {
    private String tipo;
    private String destinatario;
    private String mensaje;
}