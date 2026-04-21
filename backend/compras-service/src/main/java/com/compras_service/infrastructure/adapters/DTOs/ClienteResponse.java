package com.compras_service.infrastructure.adapters.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse {
    private Long id;
    private String nombre;
    private String nit;
    private String ciudad;
    private String pais;
    private Long usuarioId;
}
