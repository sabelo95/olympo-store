package com.compras_service.infrastructure.adapters.repository.Producto;

import com.compras_service.domain.model.Producto;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.infrastructure.adapters.DTOs.ProductoResponse;
import com.compras_service.infrastructure.adapters.mapper.ProductoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ProductoGatewayImpl implements ProductoGateway {

    private final RestTemplate restTemplate;

    @Override
    public Optional<List<Producto>> obtenerProductoPorId(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("La lista de IDs no puede estar vacía ni contener nulos");
        }

        String url = "http://localhost:8083/productos/lista-ids?ids=" +
                ids.stream().map(String::valueOf).collect(Collectors.joining(","));

        try {
            ResponseEntity<ProductoResponse[]> response = restTemplate.getForEntity(
                    url,
                    ProductoResponse[].class
            );

            System.out.println("URL construida: " + url);
            System.out.println("Respuesta completa: " + response);

            if (response.getBody() == null || response.getBody().length == 0) {
                return Optional.empty();
            }

            List<Producto> productos = Arrays.stream(response.getBody())
                    .map(ProductoMapper::toDomain)
                    .collect(Collectors.toList());

            return Optional.of(productos);

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Error consultando productos en: " + url, e);
        }
    }

    @Override
    public void reducirStock(Map<Long, Integer> productos) {
        if (productos == null || productos.isEmpty() || productos.keySet().stream().anyMatch(Objects::isNull) || productos.values().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("El mapa de productos no puede estar vacío ni contener nulos");
        }

        String url = "http://localhost:8083/productos/reducir-stock";

        try {
            restTemplate.postForEntity(url, productos, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error reduciendo stock en: " + url, e);
        }
    }

    @Override
    public void reponerStock(Map<Long, Integer> productos) {
        if (productos == null || productos.isEmpty() || productos.keySet().stream().anyMatch(Objects::isNull) || productos.values().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("El mapa de productos no puede estar vacío ni contener nulos");
        }

        String url = "http://localhost:8083/productos/reposicion-stock";

        try {
            restTemplate.postForEntity(url, productos, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en reposicion de stock en: " + url, e);
        }
    }
}
