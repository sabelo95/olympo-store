package com.compras_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cupon {
    private Long id;
    private String codigo;
    private Integer descuentoPorcentaje;
    private Long descuentoFijo;
    private boolean activo;
    private LocalDate fechaExpiracion;
    private Integer usoMaximo;
    private Integer usoActual;
    private Long montoMinimo;
}
