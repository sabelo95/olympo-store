package com.compras_service.domain.gateways;

import com.compras_service.domain.model.Producto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductoGateway {


    Optional<List<Producto>> obtenerProductoPorId(List<Long> ids);

    void reducirStock(Map<Long, Integer> productos);

    void reponerStock(Map<Long, Integer> productos);

}
