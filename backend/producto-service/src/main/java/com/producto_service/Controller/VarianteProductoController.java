package com.producto_service.Controller;

import com.producto_service.Model.Producto;
import com.producto_service.Model.VarianteProducto;
import com.producto_service.Service.VarianteProductoService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/variantes-producto")
public class VarianteProductoController {

    private final VarianteProductoService  varianteProductoService;

    @GetMapping("/{productoId}")
    public List<VarianteProducto> obtenerVariantesProducto(@PathVariable Long productoId) {
        return varianteProductoService.obtenerProductoPorId(productoId);
    }


}
