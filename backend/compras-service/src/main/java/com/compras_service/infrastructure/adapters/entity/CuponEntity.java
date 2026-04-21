package com.compras_service.infrastructure.adapters.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cupon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private Integer descuentoPorcentaje;

    private Long descuentoFijo;

    @Column(nullable = false)
    private boolean activo;

    private LocalDate fechaExpiracion;

    private Integer usoMaximo;

    @Column(nullable = false)
    private Integer usoActual;

    private Long montoMinimo;
}
