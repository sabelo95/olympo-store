package com.compras_service.domain.services;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.domain.model.Producto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CarritoAbandonadoService {

    public boolean esAbandonado(Carrito carrito) {
        return Boolean.TRUE.equals(carrito.getAbandonado());
    }

    public String generarResumenProductos(List<CarritoProducto> carritoProductos, List<Producto> productos) {
        return carritoProductos.stream()
                .map(item -> {
                    Producto producto = productos.stream()
                            .filter(p -> p.getId().equals(item.getProducto_id()))
                            .findFirst()
                            .orElse(null);

                    if (producto == null) {
                        return "<tr><td style='padding:10px 12px;font-size:14px;color:#2d3748;border-bottom:1px solid #e2e8f0;'>Producto no disponible</td>"
                                + "<td style='padding:10px 12px;text-align:center;font-size:14px;color:#4a5568;border-bottom:1px solid #e2e8f0;'>" + item.getCantidad() + "</td>"
                                + "<td style='padding:10px 12px;text-align:right;font-size:14px;color:#2d3748;border-bottom:1px solid #e2e8f0;'>-</td></tr>";
                    }

                    return "<tr>"
                            + "<td style='padding:10px 12px;font-size:14px;color:#2d3748;border-bottom:1px solid #e2e8f0;'>" + producto.getNombre() + "</td>"
                            + "<td style='padding:10px 12px;text-align:center;font-size:14px;color:#4a5568;border-bottom:1px solid #e2e8f0;'>" + item.getCantidad() + "</td>"
                            + "<td style='padding:10px 12px;text-align:right;font-size:14px;font-weight:bold;color:#2d3748;border-bottom:1px solid #e2e8f0;'>$" + String.format("%,.2f", producto.getPrecio() * item.getCantidad()) + "</td>"
                            + "</tr>";
                })
                .reduce("", String::concat);
    }

    public String construirMensaje(String resumenFilas) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='margin:0;padding:0;background:#f5f7fa;font-family:Arial,sans-serif;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='background:#f5f7fa;padding:32px 0;'>");
        html.append("<tr><td align='center'>");
        html.append("<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.08);'>");

        // Header
        html.append("<tr><td style='background:linear-gradient(135deg,#1a365d,#2b6cb0);padding:32px;text-align:center;'>");
        html.append("<h1 style='color:#ffffff;margin:0;font-size:24px;letter-spacing:1px;'>OLYMPO STORE</h1>");
        html.append("<p style='color:#bee3f8;margin:8px 0 0;font-size:14px;'>¡Olvidaste algo en tu carrito!</p>");
        html.append("</td></tr>");

        // Mensaje principal
        html.append("<tr><td style='padding:32px 40px 16px;text-align:center;'>");
        html.append("<div style='font-size:48px;'>&#128722;</div>");
        html.append("<h2 style='color:#2d3748;margin:12px 0 8px;font-size:20px;'>Tu carrito te está esperando</h2>");
        html.append("<p style='color:#718096;margin:0;font-size:15px;'>Dejaste estos productos sin completar tu compra:</p>");
        html.append("</td></tr>");

        // Tabla de productos
        html.append("<tr><td style='padding:8px 40px 24px;'>");
        html.append("<table width='100%' cellpadding='0' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr style='background:#ebf8ff;'>");
        html.append("<th style='padding:10px 12px;text-align:left;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Producto</th>");
        html.append("<th style='padding:10px 12px;text-align:center;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Cant.</th>");
        html.append("<th style='padding:10px 12px;text-align:right;font-size:13px;color:#2b6cb0;border-bottom:2px solid #bee3f8;'>Subtotal</th>");
        html.append("</tr>");
        html.append(resumenFilas);
        html.append("</table>");
        html.append("</td></tr>");

        // CTA
        html.append("<tr><td style='padding:0 40px 32px;text-align:center;'>");
        html.append("<p style='color:#718096;font-size:14px;margin:0 0 16px;'>¡Vuelve pronto y completa tu compra antes de que se agoten!</p>");
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
