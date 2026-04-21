package com.compras_service.infrastructure.adapters.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para crear o actualizar un cupón")
public class CuponRequest {

    @NotBlank(message = "El código es obligatorio")
    @Schema(description = "Código único del cupón", example = "DESCUENTO10", required = true)
    private String codigo;

    @Schema(description = "Porcentaje de descuento (ej: 15 = 15%). Exclusivo con descuentoFijo.", example = "10")
    private Integer descuentoPorcentaje;

    @Schema(description = "Descuento fijo en pesos. Exclusivo con descuentoPorcentaje.", example = "5000")
    private Long descuentoFijo;

    @NotNull(message = "El estado activo es obligatorio")
    @Schema(description = "Si el cupón está activo", example = "true", required = true)
    private Boolean activo;

    @Schema(description = "Fecha de expiración (yyyy-MM-dd). Null = sin expiración.", example = "2027-12-31")
    private LocalDate fechaExpiracion;

    @Schema(description = "Número máximo de usos. Null = ilimitado.", example = "100")
    private Integer usoMaximo;

    @Schema(description = "Monto mínimo de compra para usar el cupón. Null = sin mínimo.", example = "50000")
    private Long montoMinimo;
}
