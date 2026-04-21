package com.compras_service.infrastructure.adapters.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
    private Double precio;
    private CategoriaResponse categoria;
    private List<DetalleProductoMarcaResponse> detalleProductoMarca;
}

