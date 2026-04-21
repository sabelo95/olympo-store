package com.compras_service.infrastructure.adapters.External;

import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReporteService {

    private final ProductoGateway productoGateway;
    private final ClienteGateway clienteGateway;

    public byte[] generarReporteSemanal(List<Pedido> pedidos, String fechaInicio, String fechaFin) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            ZonedDateTime fechaColombia = ZonedDateTime.now(ZoneId.of("America/Bogota"));
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Ventas Semanal", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Periodo: " + fechaInicio + " a " + fechaFin));
            document.add(new Paragraph("Fecha de generación: " + fechaColombia));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));


            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 2, 4});


            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            String[] headers = {"ID Pedido", "Fecha", "Total", "Productos"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h, headFont));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(header);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


            List<Long> idsProductos = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                if (pedido.getDetallePedido() != null) {
                    for (DetallePedido detalle : pedido.getDetallePedido()) {
                        idsProductos.add(detalle.getProducto_id());
                    }
                }
            }

            List<String> clientesFrecuentesNombres = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            Pedido::getClienteId,
                            Collectors.counting()
                    ))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .map(id -> clienteGateway.obtenerClientePorId(id)
                            .map(Cliente::getNombre)
                            .orElse("Desconocido (" + id + ")")) // por si no existe
                    .collect(Collectors.toList());


            List<Producto> productosNombres = new ArrayList<>();
            try {
                productosNombres = productoGateway.obtenerProductoPorId(idsProductos)
                        .orElseThrow(() -> new IllegalArgumentException("Uno o más productos no existen"));
            } catch (Exception e) {
                System.out.println("⚠️ Error obteniendo productos para el reporte: " + e.getMessage());
            }

            Producto productoMasVendido = productosNombres.stream()
                    .max((p1, p2) -> Long.compare(
                            pedidos.stream().flatMap(p -> p.getDetallePedido().stream())
                                    .filter(d -> d.getProducto_id().equals(p1.getId()))
                                    .mapToLong(DetallePedido::getCantidad).sum(),
                            pedidos.stream().flatMap(p -> p.getDetallePedido().stream())
                                    .filter(d -> d.getProducto_id().equals(p2.getId()))
                                    .mapToLong(DetallePedido::getCantidad).sum()
                    )).orElse(null);


            Map<Long, String> mapaProductos = productosNombres.stream()
                    .collect(Collectors.toMap(Producto::getId, Producto::getNombre));

            double totalGeneral = 0.0;


            for (Pedido pedido : pedidos) {
                table.addCell(String.valueOf(pedido.getId()));
                table.addCell(pedido.getFechaPedido() != null ? pedido.getFechaPedido().format(formatter) : "-");
                table.addCell(String.format("$ %.2f", pedido.getTotal()));
                totalGeneral += pedido.getTotal() != null ? pedido.getTotal() : 0.0;


                StringBuilder productosTexto = new StringBuilder();
                if (pedido.getDetallePedido() != null) {
                    for (DetallePedido d : pedido.getDetallePedido()) {
                        String nombreProducto = mapaProductos.getOrDefault(d.getProducto_id(), "Producto desconocido");
                        productosTexto.append(nombreProducto)
                                .append(" (x").append(d.getCantidad()).append(")\n");
                    }
                }

                PdfPCell productosCell = new PdfPCell(new Phrase(productosTexto.toString()));
                table.addCell(productosCell);
            }


            document.add(table);
            document.add(new Paragraph(" "));


            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph total = new Paragraph("Total general de ventas: $" + String.format("%.2f", totalGeneral), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);


            if (productoMasVendido != null) {
                Paragraph masVendido = new Paragraph("Producto más vendido: " + productoMasVendido.getNombre(), totalFont);
                masVendido.setAlignment(Element.ALIGN_RIGHT);
                document.add(masVendido);
            }


            if (!clientesFrecuentesNombres.isEmpty()) {
                Paragraph clientesFrecuentes = new Paragraph(
                        "Clientes más frecuentes: " + String.join(", ", clientesFrecuentesNombres),
                        totalFont
                );
                clientesFrecuentes.setAlignment(Element.ALIGN_RIGHT);
                document.add(clientesFrecuentes);
            }

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF de reporte semanal", e);
        }
    }

}