package com.compras_service.infrastructure.adapters.DTOs;
import com.compras_service.domain.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetalleProductoMarcaResponse {

    private Long id;
    private Producto producto;
    private MarcaResponse marca;
    private int cantidad;
    private double precio;


    }


