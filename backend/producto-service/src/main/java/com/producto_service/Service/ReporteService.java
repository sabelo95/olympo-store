package com.producto_service.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.producto_service.Model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ProductoService productoService;

    public byte[] generarReporteInventarioBajo(int limite) throws Exception {
        List<Producto> productos = productoService.obtenerProductosConStockMenorA(limite);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // 🔹 Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte de Inventario Bajo", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Fecha de generación: " + new Date()));
        document.add(new Paragraph("Límite de inventario: " + limite));
        document.add(new Paragraph(" "));

        // 🔹 Tabla
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 3, 2, 2});

        // Encabezados
        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String[] headers = {"Nombre", "Categoría", "Precio", "Cantidad"};
        for (String h : headers) {
            PdfPCell header = new PdfPCell(new Phrase(h, headFont));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(header);
        }

        // Filas
        for (Producto p : productos) {
            table.addCell(p.getNombre());
            table.addCell(p.getCategoria().getNombre());
            table.addCell(String.format("$ %.2f", p.getPrecio()));
            table.addCell(String.valueOf(p.getCantidad()));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}

