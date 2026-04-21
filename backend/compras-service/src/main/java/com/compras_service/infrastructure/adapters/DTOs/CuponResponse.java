package com.compras_service.infrastructure.adapters.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CuponResponse {
    private Long id;
    private String codigo;
    private Integer descuentoPorcentaje;
    private Long descuentoFijo;
    private boolean activo;
    private LocalDate fechaExpiracion;
    private Integer usoMaximo;
    private Integer usoActual;
    private Long montoMinimo;
    // Campos para el flujo de validación en checkout
    private boolean valido;
    private String mensaje;
}
