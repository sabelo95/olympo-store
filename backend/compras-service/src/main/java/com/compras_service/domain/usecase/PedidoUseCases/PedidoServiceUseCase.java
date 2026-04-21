// java
package com.compras_service.domain.usecase.PedidoUseCases;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.Pedido.DetallePedidoGateway;
import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PedidoServiceUseCase {

    private final PedidoGateway pedidoGateway;
    private final ProductoGateway productoGateway;
    private final CarritoGateway carritoGateway;
    private final DetallePedidoGateway detallePedidoGateway;

    public Pedido crearPedidoDesdeCarrito(Long clienteId, Long carritoId, String direccionEnvio, String metodoPago) {
        Carrito carrito = carritoGateway.obtenerCarritoPorId(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("El carrito con id " + carritoId + " no existe"));

        List<Carrito> carritosCliente = carritoGateway.obtenerCarritosPorClienteId(clienteId);
        if (!carritosCliente.contains(carrito)) {
            throw new IllegalArgumentException("El carrito con id " + carritoId + " no pertenece al cliente con id " + clienteId);
        }

        carrito.setAbandonado(false);
        carritoGateway.actualizarCarrito(carrito);

        List<CarritoProducto> productosPedido = carrito.getProductos();
        if (productosPedido == null || productosPedido.isEmpty()) {
            throw new IllegalArgumentException("El carrito con id " + carritoId + " no tiene productos");
        }

        List<Long> idsProductos = new ArrayList<>();
        for (CarritoProducto cp : productosPedido) {
            idsProductos.add(cp.getProducto_id());
        }

        List<Producto> productos = productoGateway.obtenerProductoPorId(idsProductos)
                .orElseThrow(() -> new IllegalArgumentException("Uno o más productos no existen"));

        double total = productos.stream()
                .mapToDouble(producto -> producto.getPrecio()
                        * productosPedido.stream()
                        .filter(cp -> cp.getProducto_id().equals(producto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("El producto con id " + producto.getId() + " no existe en el carrito"))
                        .getCantidad())
                .sum();

        Pedido pedido = Pedido.builder()
                .clienteId(clienteId)
                .fechaPedido(LocalDateTime.now())
                .estado(EstadoPedido.CREADO)
                .direccionEnvio(direccionEnvio)
                .total(total)
                .fechaEntregaEstimada(LocalDateTime.now().plusDays(7))
                .fechaEntregaReal(null)
                .detallePedido(new ArrayList<>())
                .metodoPago(metodoPago)
                .build();

        Pedido pedidoGuardado = pedidoGateway.crearPedido(pedido);

        List<DetallePedido> detallesPedido = new ArrayList<>();
        for (Producto producto : productos) {
            Integer cantidad = productosPedido.stream()
                    .filter(cp -> cp.getProducto_id().equals(producto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("El producto con id " + producto.getId() + " no existe en el carrito"))
                    .getCantidad();

            DetallePedido detallePedido = DetallePedido.builder()
                    .pedidoId(pedidoGuardado.getId())
                    .producto_id(producto.getId())
                    .cantidad(cantidad)
                    .precio_unitario(producto.getPrecio())
                    .subtotal(producto.getPrecio() * cantidad)
                    .build();

            detallesPedido.add(detallePedido);
            detallePedidoGateway.crearDetallePedido(detallePedido);
        }

        // Asignar detalles al pedido guardado (si la entidad lo permite)
        pedidoGuardado.setDetallePedido(detallesPedido);

        return pedidoGuardado;
    }
}
