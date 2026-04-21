package com.producto_service.Service;

import com.producto_service.Config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ReporteAutoTask {

    private final ReporteService reporteService;
    private final NotificacionService notificacionService;
    private final JwtUtil jwtUtil;

    // Cada 15 minutos (en los minutos 0, 15, 30, 45)
    @Scheduled(cron = "0 0,15,30,45 * * * *", zone = "America/Bogota")
    public void generarReporteAutomatico() {
        String correoUsuario = jwtUtil.obtenerCorreoActual();
        String fecha = ZonedDateTime.now(ZoneId.of("America/Bogota"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        try {
            // Generar PDF
            byte[] pdf = reporteService.generarReporteInventarioBajo(10);

            // Subir a S3
            S3Client s3 = S3Client.builder()
                    .region(Region.US_EAST_2)
                    .build();

            String nombreArchivo = "reportes/reporte_inventario_" + fecha + ".pdf";

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket("arka-reportes")
                            .key(nombreArchivo)
                            .contentType("application/pdf")
                            .build(),
                    RequestBody.fromBytes(pdf)
            );

            System.out.println("✅ Reporte guardado: s3://arka-reportes/" + nombreArchivo);

            // Enviar email
            notificacionService.enviarNotificacionConAdjunto(
                    "EMAIL",
                    "santiagoberriolopez@gmail.com",
                    "Reporte de inventario bajo generado.\nArchivo: " + nombreArchivo,
                    pdf,
                    "reporte_inventario_bajo.pdf"
            );

        } catch (Exception e) {
            System.err.println("❌ Error generando reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
