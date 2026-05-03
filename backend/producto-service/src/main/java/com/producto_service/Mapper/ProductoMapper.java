package com.producto_service.Mapper;

import com.producto_service.DTO.ProductoResponseDto;
import com.producto_service.Model.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ProductoMapper {

    public ProductoResponseDto toDto(Producto producto) {
        ProductoResponseDto dto = new ProductoResponseDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setTamano(producto.getTamano());
        dto.setSabor(producto.getSabor());
        dto.setCantidad(producto.getCantidad());
        dto.setPrecio(producto.getPrecio());
        dto.setCosto(producto.getCosto());
        dto.setImagenGeneral(producto.getImagenGeneral());
        dto.setImagenNutricional(producto.getImagenNutricional());
        dto.setCategoria(producto.getCategoria());
        dto.setMarca(producto.getMarca());
        return dto;
    }
}
