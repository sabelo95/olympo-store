package com.compras_service.domain.usecase.PedidoUseCases;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.gateways.Pedido.PedidoGateway;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.DetallePedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.Producto;
import com.compras_service.domain.services.StockService;
import com.compras_service.domain.services.EstadoPedidoService;
import com.compras_service.domain.validators.PedidoValidator;
import com.compras_service.infrastructure.adapters.DTOs.ActualizarPedidoRequest;
import com.compras_service.infrastructure.adapters.DTOs.DetallePedidoRequest;
import com.compras_service.infrastructure.adapters.External.NotificacionService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ActualizarPedidoUseCase {

    private final PedidoGateway pedidoGateway;
    private final StockService stockService;
    private final PedidoValidator pedidoValidator;
    private final EstadoPedidoService estadoPedidoService;
    private final ClienteGateway clienteGateway;
    private final NotificacionService notificacionService;
    private final ProductoGateway productoGateway;


    public Pedido actualizarPedido(ActualizarPedidoRequest request) {


        Pedido pedido = pedidoGateway.obtenerPedidoPorId(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("El pedido con id " + request.getId() + " no existe"));


        pedidoValidator.validarEstadoEditable(pedido);


        List<DetallePedido> nuevosDetalles = construirDetallesPedido(request.getDetalles());


        stockService.validarPedidoConAnterior(nuevosDetalles, pedido);


        stockService.reponerStockDelPedido(pedido);


        stockService.reducirStockDelPedido(nuevosDetalles);


        pedido.getDetallePedido().clear();

        for (DetallePedido d : nuevosDetalles) {
            d.setPedidoId(pedido.getId());
            pedido.getDetallePedido().add(d);
        }

        // 8. Recalcular el total
        double total = pedido.getDetallePedido().stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();

        pedido.setTotal(total);
        pedido.setDireccionEnvio(request.getDireccionEnvio());

        // 9. Guardar cambios
        return pedidoGateway.actualizarPedido(pedido);
    }


    private List<DetallePedido> construirDetallesPedido(List<DetallePedidoRequest> detallesRequest) {

        // Obtener IDs de productos
        List<Long> productosIds = detallesRequest.stream()
                .map(DetallePedidoRequest::getProductoId)
                .toList();

        // Obtener productos desde el gateway
        List<Producto> productos = productoGateway.obtenerProductoPorId(productosIds)
                .orElseThrow(() -> new IllegalArgumentException("Error al obtener información de productos"));

        // Crear un mapa para acceso rápido
        Map<Long, Producto> productosMap = productos.stream()
                .collect(Collectors.toMap(Producto::getId, p -> p));

        // Construir detalles con precios
        List<DetallePedido> detalles = new ArrayList<>();

        for (DetallePedidoRequest detalle : detallesRequest) {
            Producto producto = productosMap.get(detalle.getProductoId());

            if (producto == null) {
                throw new IllegalArgumentException(
                        "El producto con ID " + detalle.getProductoId() + " no existe"
                );
            }

            DetallePedido detallePedido = DetallePedido.builder()
                    .producto_id(producto.getId())
                    .cantidad(detalle.getCantidad())
                    .precio_unitario(producto.getPrecio())
                    .subtotal(producto.getPrecio() * detalle.getCantidad())
                    .build();

            detalles.add(detallePedido);
        }

        return detalles;
    }


    public Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {

        // 1. Obtener el pedido
        Pedido pedido = pedidoGateway.obtenerPedidoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con id " + id + " no existe"));

        // 2. Cambiar el estado
        estadoPedidoService.cambiarEstado(pedido, nuevoEstado);

        // 3. Notificar al cliente
        notificarCambioEstado(pedido, nuevoEstado);

        // 4. Guardar cambios
        return pedidoGateway.actualizarPedido(pedido);
    }

    private void notificarCambioEstado(Pedido pedido, EstadoPedido nuevoEstado) {
        try {
            String correo = clienteGateway.obtenerEmailPorId(pedido.getClienteId())
                    .orElseThrow(() -> new RuntimeException("No se encontró correo del cliente"));

            notificacionService.enviarNotificacion(
                    "Actualización de tu pedido #" + pedido.getId(),
                    correo,
                    generarHtmlCambioEstado(pedido, nuevoEstado)
            );

        } catch (Exception e) {
            System.out.println("Error enviando notificación: " + e.getMessage());
        }
    }

    private String generarHtmlCambioEstado(Pedido pedido, EstadoPedido nuevoEstado) {
        String color;
        String icono;
        String titulo;
        String descripcion;

        switch (nuevoEstado) {
            case EN_PROCESO -> {
                color = "#d69e2e";
                icono = "&#9201;";
                titulo = "Tu pedido está en proceso";
                descripcion = "Estamos preparando tu pedido. Te notificaremos cuando esté listo para envío.";
            }
            case COMPLETADO -> {
                color = "#38a169";
                icono = "&#10003;";
                titulo = "¡Tu pedido fue entregado!";
                descripcion = "Tu pedido ha sido entregado exitosamente. Esperamos que disfrutes tu compra.";
            }
            case CANCELADO -> {
                color = "#e53e3e";
                icono = "&#10005;";
                titulo = "Tu pedido fue cancelado";
                descripcion = "Tu pedido ha sido cancelado. Si tienes dudas, contáctanos.";
            }
            default -> {
                color = "#2b6cb0";
                icono = "&#128722;";
                titulo = "Pedido recibido";
                descripcion = "Hemos recibido tu pedido y lo estamos revisando.";
            }
        }

        String badgeBg = color + "1a";

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='margin:0;padding:0;background:#f5f7fa;font-family:Arial,sans-serif;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f5f7fa;padding:32px 0;'>");
        html.append("<tr><td align='center'>");
        html.append("<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.08);'>");

        // Header
        html.append("<tr><td style='background:linear-gradient(135deg,#1a365d,#2b6cb0);padding:32px;text-align:center;'>");
        html.append("<h1 style='color:#ffffff;margin:0;font-size:24px;letter-spacing:1px;'>OLYMPO STORE</h1>");
        html.append("<p style='color:#bee3f8;margin:8px 0 0;font-size:14px;'>Actualización de tu pedido</p>");
        html.append("</td></tr>");

        // Icono de estado
        html.append("<tr><td style='padding:40px 40px 8px;text-align:center;'>");
        html.append("<div style='display:inline-block;width:72px;height:72px;border-radius:50%;background:").append(badgeBg).append(";border:3px solid ").append(color).append(";line-height:72px;font-size:32px;color:").append(color).append(";'>").append(icono).append("</div>");
        html.append("</td></tr>");

        // Título y descripción
        html.append("<tr><td style='padding:16px 40px 8px;text-align:center;'>");
        html.append("<h2 style='color:#2d3748;margin:0 0 8px;font-size:22px;'>").append(titulo).append("</h2>");
        html.append("<p style='color:#718096;margin:0;font-size:15px;'>").append(descripcion).append("</p>");
        html.append("</td></tr>");

        // Badge de estado
        html.append("<tr><td style='padding:16px 40px 24px;text-align:center;'>");
        html.append("<span style='display:inline-block;padding:6px 20px;border-radius:20px;background:").append(badgeBg).append(";color:").append(color).append(";font-size:13px;font-weight:bold;letter-spacing:1px;border:1px solid ").append(color).append(";'>").append(nuevoEstado.name().replace("_", " ")).append("</span>");
        html.append("</td></tr>");

        // Detalle del pedido
        html.append("<tr><td style='padding:0 40px 32px;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f7fafc;border-radius:6px;border:1px solid #e2e8f0;'>");
        html.append("<tr><td style='padding:20px;'>");
        html.append("<p style='margin:0 0 8px;font-size:13px;color:#718096;'><strong style='color:#2d3748;'>Pedido #</strong>").append(pedido.getId()).append("</p>");
        html.append("<p style='margin:0 0 8px;font-size:13px;color:#718096;'><strong style='color:#2d3748;'>Total: </strong>$").append(String.format("%,.2f", pedido.getTotal())).append("</p>");
        html.append("<p style='margin:0 0 8px;font-size:13px;color:#718096;'><strong style='color:#2d3748;'>Dirección: </strong>").append(pedido.getDireccionEnvio()).append("</p>");
        if (pedido.getFechaEntregaEstimada() != null) {
            html.append("<p style='margin:0;font-size:13px;color:#718096;'><strong style='color:#2d3748;'>Entrega estimada: </strong>").append(pedido.getFechaEntregaEstimada().toLocalDate()).append("</p>");
        }
        html.append("</td></tr></table>");
        html.append("</td></tr>");

        // Footer
        html.append("<tr><td style='background:#f7fafc;padding:20px 40px;text-align:center;border-top:1px solid #e2e8f0;'>");
        html.append("<p style='margin:0;font-size:12px;color:#a0aec0;'>Este es un correo automático, por favor no responder.</p>");
        html.append("<p style='margin:4px 0 0;font-size:12px;color:#a0aec0;'>© OLYMPO STORE</p>");
        html.append("</td></tr>");

        html.append("</table>");
        html.append("</td></tr></table>");
        html.append("</body></html>");
        return html.toString();
    }
}