package com.compras_service.domain.services;

import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.domain.model.DetallePedido;
import com.compras_service.domain.model.Pedido;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StockService {

    private final ProductoGateway productoGateway;

    // -------------------------------------------------------------------------
    // VALIDACIONES
    // -------------------------------------------------------------------------


    public void validarCarrito(Carrito carrito) {
        validarCarritoNoVacio(carrito);
        validarExistenciaDeProductos(carrito);
        validarCantidades(carrito);
        validarStockDisponible(carrito, null);
    }


    public void validarCarritoConAnterior(Carrito carritoNuevo, Carrito carritoAnterior) {
        validarCarritoNoVacio(carritoNuevo);
        validarExistenciaDeProductos(carritoNuevo);
        validarCantidades(carritoNuevo);
        validarStockDisponible(carritoNuevo, carritoAnterior);
    }

    private void validarCarritoNoVacio(Carrito carrito) {
        if (carrito.getProductos() == null || carrito.getProductos().isEmpty()) {
            throw new IllegalArgumentException("El carrito debe tener al menos un producto");
        }
    }

    private void validarExistenciaDeProductos(Carrito carrito) {
        List<Long> ids = carrito.getProductos()
                .stream()
                .map(CarritoProducto::getProducto_id)
                .toList();

        var productosOpt = productoGateway.obtenerProductoPorId(ids);

        if (productosOpt.isEmpty() || productosOpt.get().size() != ids.size()) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }
    }

    private void validarCantidades(Carrito carrito) {
        if (carrito.getProductos()
                .stream()
                .anyMatch(p -> p.getCantidad() <= 0)) {

            throw new IllegalArgumentException(
                    "La cantidad de un producto debe ser mayor que cero"
            );
        }
    }

    private void validarStockDisponible(Carrito carritoNuevo, Carrito carritoAnterior) {
        // Obtener todas las IDs necesarias
        List<Long> ids = carritoNuevo.getProductos()
                .stream()
                .map(CarritoProducto::getProducto_id)
                .toList();

        var productos = productoGateway.obtenerProductoPorId(ids)
                .orElseThrow(() ->
                        new IllegalArgumentException("Error inesperado obteniendo productos"));


        Map<Long, Integer> cantidadesAnteriores = new HashMap<>();
        if (carritoAnterior != null && carritoAnterior.getProductos() != null) {
            cantidadesAnteriores = carritoAnterior.getProductos().stream()
                    .collect(Collectors.toMap(
                            CarritoProducto::getProducto_id,
                            CarritoProducto::getCantidad
                    ));
        }


        for (CarritoProducto p : carritoNuevo.getProductos()) {
            var producto = productos.stream()
                    .filter(prod -> prod.getId().equals(p.getProducto_id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El producto con id " + p.getProducto_id() + " no existe"
                    ));


            int stockActual = producto.getCantidad();


            int cantidadReservadaAnterior = cantidadesAnteriores.getOrDefault(p.getProducto_id(), 0);


            int stockDisponible = stockActual + cantidadReservadaAnterior;


            if (p.getCantidad() > stockDisponible) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto '" + producto.getNombre() +
                                "'. Disponible: " + stockDisponible +
                                ", solicitado: " + p.getCantidad()
                );
            }
        }
    }



    public void validarPedido(List<DetallePedido> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto");
        }

        List<Long> ids = detalles.stream()
                .map(DetallePedido::getProducto_id)
                .toList();

        var productos = productoGateway.obtenerProductoPorId(ids)
                .orElseThrow(() ->
                        new IllegalArgumentException("Error obteniendo productos"));

        for (DetallePedido detalle : detalles) {
            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad de un producto debe ser mayor que cero"
                );
            }

            var producto = productos.stream()
                    .filter(p -> p.getId().equals(detalle.getProducto_id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El producto con id " + detalle.getProducto_id() + " no existe"
                    ));

            if (detalle.getCantidad() > producto.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto '" + producto.getNombre() +
                                "'. Disponible: " + producto.getCantidad() +
                                ", solicitado: " + detalle.getCantidad()
                );
            }
        }
    }


    public void validarPedidoConAnterior(List<DetallePedido> nuevosDetalles, Pedido pedidoAnterior) {
        if (nuevosDetalles == null || nuevosDetalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto");
        }

        List<Long> ids = nuevosDetalles.stream()
                .map(DetallePedido::getProducto_id)
                .toList();

        var productos = productoGateway.obtenerProductoPorId(ids)
                .orElseThrow(() ->
                        new IllegalArgumentException("Error obteniendo productos"));


        Map<Long, Integer> cantidadesAnteriores = new HashMap<>();
        if (pedidoAnterior != null && pedidoAnterior.getDetallePedido() != null) {
            for (DetallePedido d : pedidoAnterior.getDetallePedido()) {
                cantidadesAnteriores.put(d.getProducto_id(), d.getCantidad());
            }
        }


        for (DetallePedido detalle : nuevosDetalles) {
            var producto = productos.stream()
                    .filter(p -> p.getId().equals(detalle.getProducto_id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El producto con id " + detalle.getProducto_id() + " no existe"
                    ));

            int stockActual = producto.getCantidad();
            int cantidadReservadaAnterior = cantidadesAnteriores.getOrDefault(detalle.getProducto_id(), 0);
            int stockDisponible = stockActual + cantidadReservadaAnterior;

            if (detalle.getCantidad() > stockDisponible) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto '" + producto.getNombre() +
                                "'. Disponible: " + stockDisponible +
                                ", solicitado: " + detalle.getCantidad()
                );
            }
        }
    }




    public void reponerStockDelCarrito(Carrito carritoActual) {
        Map<Long, Integer> productosAReponer = carritoActual.getProductos().stream()
                .collect(Collectors.toMap(
                        CarritoProducto::getProducto_id,
                        CarritoProducto::getCantidad
                ));

        productoGateway.reponerStock(productosAReponer);
    }


    public void reducirStockDelCarrito(Carrito carritoNuevo) {
        Map<Long, Integer> productosAReducir = carritoNuevo.getProductos().stream()
                .collect(Collectors.toMap(
                        CarritoProducto::getProducto_id,
                        CarritoProducto::getCantidad
                ));

        productoGateway.reducirStock(productosAReducir);
    }


    public void reponerStockDelPedido(Pedido pedido) {
        Map<Long, Integer> productos = new HashMap<>();

        for (DetallePedido d : pedido.getDetallePedido()) {
            productos.put(d.getProducto_id(), d.getCantidad());
        }

        productoGateway.reponerStock(productos);
    }

    public void reducirStockDelPedido(List<DetallePedido> nuevosDetalles) {
        Map<Long, Integer> productos = new HashMap<>();

        for (DetallePedido d : nuevosDetalles) {
            productos.put(d.getProducto_id(), d.getCantidad());
        }

        productoGateway.reducirStock(productos);
    }
}
