package com.compras_service.infrastructure.adapters.mapper;

import com.compras_service.domain.model.Producto;
import com.compras_service.infrastructure.adapters.DTOs.ProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

   public static  Producto toDomain(ProductoResponse productoResponse) {

       String marca = "";
       if (productoResponse.getDetalleProductoMarca() != null && !productoResponse.getDetalleProductoMarca().isEmpty()) {
           marca = productoResponse.getDetalleProductoMarca().get(0).getMarca().getNombre();
       }

        Producto producto = new Producto();
        producto.setId(productoResponse.getId());
        producto.setNombre(productoResponse.getNombre());
        producto.setDescripcion(productoResponse.getDescripcion());
        producto.setCantidad(productoResponse.getCantidad());
        producto.setPrecio(productoResponse.getPrecio());
        producto.setCategoria(productoResponse.getCategoria().getNombre());



        return producto;
    }
}
