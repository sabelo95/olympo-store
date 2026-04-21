package com.compras_service.infrastructure.adapters.External;

import com.compras_service.infrastructure.adapters.DTOs.NotificacionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class NotificacionService {

    private final WebClient webClient;

    public NotificacionService(WebClient.Builder builder,
                               @Value("${notificacion.service.url}") String notificacionServiceUrl) {
        this.webClient = builder.baseUrl(notificacionServiceUrl).build();
    }

    public void enviarNotificacion(String tipo, String destinatario, String mensaje) {
        NotificacionRequest request = new NotificacionRequest(tipo, destinatario, mensaje);

        webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(respuesta -> System.out.println("✅ Respuesta del servicio de notificaciones: " + respuesta))
                .doOnError(error -> System.err.println("❌ Error al enviar notificación: " + error.getMessage()))
                .block();
    }

    public void enviarNotificacionConAdjunto(String tipo,
                                             String destinatario,
                                             String mensaje,
                                             byte[] archivo,
                                             String nombreArchivo) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("tipo", tipo);
        builder.part("destinatario", destinatario);
        builder.part("mensaje", mensaje);

        if (archivo != null && archivo.length > 0) {
            builder.part("archivo", new ByteArrayResource(archivo))
                    .header("Content-Disposition",
                            "form-data; name=archivo; filename=" + nombreArchivo);
        }

        MultiValueMap<String, HttpEntity<?>> multipartData = builder.build();

        webClient.post()
                .uri("/con-adjunto") // tu endpoint con @PostMapping(consumes = "multipart/form-data")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(respuesta -> System.out.println("📨 Respuesta del servicio de notificaciones: " + respuesta))
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException ex) {
                        System.err.println("❌ Error HTTP: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    } else {
                        System.err.println("❌ Error al enviar notificación: " + error.getMessage());
                    }
                })
                .block();
    }
}
