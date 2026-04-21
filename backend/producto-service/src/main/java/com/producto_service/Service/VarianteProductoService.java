package com.producto_service.Service;

import com.producto_service.Model.VarianteProducto;
import com.producto_service.Repository.VarianteProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;

    public List<VarianteProducto> obtenerProductoPorId(Long id) {
        return varianteProductoRepository.findByProductoId(id);
    }
}
