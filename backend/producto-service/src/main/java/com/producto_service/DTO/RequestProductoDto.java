package com.producto_service.DTO;

import com.producto_service.Model.Categoria;
import com.producto_service.Model.Marca;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductoDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    private String descripcion;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "La categoría no puede ser nula")
    @Valid
    private Categoria categoria;

    @NotNull(message = "La marca no puede ser nula")
    @Valid
    private Marca marca;

    @NotBlank(message = "El tamaño no puede estar vacío")
    private String tamano;  // "500g", "1kg"

    @NotBlank(message = "El sabor no puede estar vacío")
    private String sabor;   // "Chocolate", "Vainilla"

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private Double precio;

    @NotNull(message = "El costo no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor que 0")
    private Double costo;

    // Campos para las imágenes (opcionales)
    @Size(max = 255, message = "El nombre de la imagen general no puede exceder 255 caracteres")
    private String imagenGeneral;

    @Size(max = 255, message = "El nombre de la imagen nutricional no puede exceder 255 caracteres")
    private String imagenNutricional;
}