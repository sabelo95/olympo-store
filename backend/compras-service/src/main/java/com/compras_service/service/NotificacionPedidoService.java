package com.compras_service.service;


import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.DetallePedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.model.Producto;
import com.compras_service.infrastructure.adapters.External.NotificacionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificacionPedidoService {

    private final ClienteGateway clienteGateway;
    private final NotificacionService notificacionService;
    private final ProductoGateway productoGateway;

    public void enviarNotificacionPedidoCreado(Pedido pedido) {
        try {
            String correoUsuario = clienteGateway
                    .obtenerEmailPorId(pedido.getClienteId())
                    .orElseThrow(() -> new RuntimeException("No se encontró correo para el cliente"));

            List<Long> ids = pedido.getDetallePedido().stream()
                    .map(DetallePedido::getProducto_id)
                    .toList();

            Map<Long, Producto> productosMap = productoGateway.obtenerProductoPorId(ids)
                    .orElse(List.of())
                    .stream()
                    .collect(Collectors.toMap(Producto::getId, p -> p));

            String html = generarHtmlPedido(pedido, productosMap);
            notificacionService.enviarNotificacion(
                    "PEDIDO CREADO CON ÉXITO",
                    correoUsuario,
                    html
            );
        } catch (Exception e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

    private String generarHtmlPedido(Pedido pedido, Map<Long, Producto> productosMap) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='margin:0;padding:0;background:#f5f7fa;font-family:Arial,sans-serif;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f5f7fa;padding:32px 0;'>");
        html.append("<tr><td align='center'>");
        html.append("<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.08);'>");

        // Header
        html.append("<tr><td style='background:linear-gradient(135deg,#1a365d,#2b6cb0);padding:32px;text-align:center;'>");
        html.append("<h1 style='color:#ffffff;margin:0;font-size:24px;letter-spacing:1px;'>OLYMPO STORE</h1>");
        html.append("<p style='color:#bee3f8;margin:8px 0 0;font-size:14px;'>¡Gracias por tu compra!</p>");
        html.append("</td></tr>");

        // Confirmación
        html.append("<tr><td style='padding:32px 40px 16px;'>");
        html.append("<h2 style='color:#2b6cb0;margin:0 0 8px;font-size:20px;'>Tu pedido ha sido confirmado</h2>");
        html.append("<p style='color:#718096;margin:0;font-size:14px;'>Pedido <strong style='color:#2d3748;'>#").append(pedido.getId()).append("</strong></p>");
        html.append("</td></tr>");

        // Tabla de productos
        html.append("<tr><td style='padding:8px 40px 24px;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr style='background:#ebf8ff;'>");
        html.append("<th style='padding:10px 12px;text-align:left;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Producto</th>");
        html.append("<th style='padding:10px 12px;text-align:center;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Cant.</th>");
        html.append("<th style='padding:10px 12px;text-align:right;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Precio</th>");
        html.append("<th style='padding:10px 12px;text-align:right;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Subtotal</th>");
        html.append("</tr>");

        boolean par = false;
        for (DetallePedido d : pedido.getDetallePedido()) {
            Producto producto = productosMap.get(d.getProducto_id());
            String nombre = producto != null ? producto.getNombre() : "Producto #" + d.getProducto_id();
            String bg = par ? "#f7fafc" : "#ffffff";
            html.append("<tr style='background:").append(bg).append(";'>")
                    .append("<td style='padding:10px 12px;font-size:14px;color:#2d3748;border-bottom:1px solid #e2e8f0;'>").append(nombre).append("</td>")
                    .append("<td style='padding:10px 12px;text-align:center;font-size:14px;color:#4a5568;border-bottom:1px solid #e2e8f0;'>").append(d.getCantidad()).append("</td>")
                    .append("<td style='padding:10px 12px;text-align:right;font-size:14px;color:#4a5568;border-bottom:1px solid #e2e8f0;'>$").append(String.format("%,.2f", d.getPrecio_unitario())).append("</td>")
                    .append("<td style='padding:10px 12px;text-align:right;font-size:14px;color:#2d3748;font-weight:bold;border-bottom:1px solid #e2e8f0;'>$").append(String.format("%,.2f", d.getSubtotal())).append("</td>")
                    .append("</tr>");
            par = !par;
        }
        html.append("</table>");
        html.append("</td></tr>");

        // Total y detalles
        html.append("<tr><td style='padding:0 40px 24px;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0'>");
        html.append("<tr><td style='text-align:right;padding-top:12px;font-size:18px;font-weight:bold;color:#1a365d;'>Total: $").append(String.format("%,.2f", pedido.getTotal())).append("</td></tr>");
        html.append("<tr><td style='text-align:right;padding-top:4px;font-size:13px;color:#718096;'>Método de pago: ").append(pedido.getMetodoPago()).append("</td></tr>");
        html.append("</table>");
        html.append("</td></tr>");

        // Entrega
        html.append("<tr><td style='padding:0 40px 32px;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f0fff4;border-radius:6px;border-left:4px solid #48bb78;'>");
        html.append("<tr><td style='padding:16px;'>");
        html.append("<p style='margin:0;font-size:13px;color:#276749;'><strong>Dirección de entrega:</strong> ").append(pedido.getDireccionEnvio()).append("</p>");
        html.append("<p style='margin:6px 0 0;font-size:13px;color:#276749;'><strong>Fecha estimada de entrega:</strong> ").append(pedido.getFechaEntregaEstimada().toLocalDate()).append("</p>");
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
